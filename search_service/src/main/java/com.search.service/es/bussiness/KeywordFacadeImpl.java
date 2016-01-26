package com.search.service.es.bussiness;

import com.alibaba.fastjson.JSON;
import com.search.service.bean.Keyword;
import com.search.service.es.AbstractFacade;
import com.search.service.es.KeywordFacade;
import com.search.service.es.util.EsUtil;
import com.search.service.es.util.Jerseys;
import com.search.utils.Constants;
import com.search.utils.StringUtils;
import com.sun.jersey.api.client.WebResource;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.common.io.Streams;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service(value = KeywordFacade.BEAN_ID)
public class KeywordFacadeImpl extends AbstractFacade<Keyword> implements KeywordFacade {

    protected final static Logger LOG = LoggerFactory.getLogger(KeywordFacadeImpl.class);

    @Resource
    EsUtil esUtil;

    @Override
    public Keyword get(Long id) {
        Keyword keyword = null;
        GetResponse gp = esUtil.getClient().prepareGet(Constants.GLOBAL_INDEX_NAME, BEAN_TYPE, id.toString())
                .execute().actionGet();
        if (gp.isExists()) {
            keyword = JSON.parseObject(gp.getSourceAsString(), Keyword.class);
        } else {
            LOG.info("Not Exists : {}", id);
        }
        return keyword;
    }

    /**
     * 初始化数据-随机字符串
     *
     * @param count
     */
    public void testInit(int count) throws Exception {
        WebResource client = null;
        try {
            String keywordMapping = Streams.copyToStringFromClasspath(Constants.ES_SEARCH_JSON_PATH + "keyword.json");
            PutMappingResponse response = esUtil.getClient().admin().indices()
                    .preparePutMapping(Constants.GLOBAL_INDEX_NAME).setType("keyword")
                    .setSource(keywordMapping).execute().actionGet();
            client = Jerseys.createClient(Constants.BASE_URL);
        } catch (ElasticsearchException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (client == null) {
                throw new Exception("client is null");
            }
        }
        for (int i = 0; i < count; i++) {
            try {
                Keyword t = new Keyword();
                int id = StringUtils.randomInt(10000, 99990);
                String word = StringUtils.randomChinese(5);
                t.setId(Long.valueOf(id));
                t.setWord(word);
                WebResource wr = client.path("/" + Constants.GLOBAL_INDEX_NAME + "/keyword/" + id);
                String json = JSON.toJSON(t).toString();
                wr.put(json);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    public List<Keyword> associateWord(String key) {
        return search(key, "", SortOrder.ASC, 1, 10).getItems();
    }

    public SearchResult<Keyword> search(String key, String sort, SortOrder order, int pageNo, int pageSize) {
        final SearchResult<Keyword> searchResult = new SearchResult<Keyword>();
        SearchRequestBuilder srb = builder(key, sort, order, pageNo, pageSize);
        SearchResponse searchResponse = srb.execute().actionGet();
        esUtil.getClient().close();
        final SearchHits hits = searchResponse.getHits();
        List<Keyword> items = new ArrayList<Keyword>();
        for (final SearchHit searchHit : hits.getHits()) {
            final Keyword keyword = JSON.parseObject(
                    searchHit.getSourceAsString(), Keyword.class);
            // 获取对应的高亮域
            Map<String, HighlightField> hig = searchHit.highlightFields();
            // 从设定的高亮域中取得指定域
            HighlightField keywordField = getHighlightField(hig);
            if (keywordField != null) {
                // 取得定义的高亮标签
                Text[] keywordFieldTexts = keywordField.fragments();
                String word = "";
                // 为name串值增加自定义的高亮标签
                for (Text text : keywordFieldTexts) {
                    word += text;
                }
                // 将追加了高亮标签的串值重新填充到对应的对象
                keyword.setWord(word);
            }
            items.add(keyword);
        }
        searchResult.setTotalHits((int) hits.getTotalHits());
        searchResult.setItems(items);
        return searchResult;
    }


    /**
     * 搜索使用 - 构建Builder
     *
     * @param key      搜索Key
     * @param sort     排序字段
     * @param order    排序顺序
     * @param pageNo   页码
     * @param pageSize 每页数据数
     * @return SearchResult<Keyword>
     */
    private SearchRequestBuilder builder(String key, String sort,
                                         SortOrder order, int pageNo, int pageSize) {
        BoolQueryBuilder bool = new BoolQueryBuilder();
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery(key,
                highlightedFields).operator(MatchQueryBuilder.Operator.AND);
        bool.must(builder);
        SearchRequestBuilder srb = esUtil.getBuilder().setTypes(BEAN_TYPE);
        // 设置查询类型
        // 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
        // 2.SearchType.SCAN = 扫描查询,无序
        srb.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        // 设置查询条件
        srb.setQuery(bool);
        if (StringUtils.isNotBlank(sort)) {
            // 排序
            srb.addSort(sort, order);
        }
        // 分页应用
        srb.setFrom((pageNo - 1) * pageSize).setSize(pageNo * pageSize);
        // 设置是否按查询匹配度排序
        srb.setExplain(false);
        // 设置高亮显示
        for (int i = 0; i < highlightedFields.length; i++) {
            srb.addHighlightedField(highlightedFields[i]);
        }
        srb.setHighlighterPreTags("<span style='color:red'>");
        srb.setHighlighterPostTags("</span>");
        return srb;
    }

    /**
     * HighlightField 高亮
     *
     * @param hig
     * @return
     */
    private HighlightField getHighlightField(Map<String, HighlightField> hig) {
        HighlightField field = hig.get(highlightedFields[0]);
        // 设置高亮显示
        // for (int i = 0; i < highlightedFields.length; i++) {
        // field = hig.get(highlightedFields[i]);
        // if (field != null) {
        // return field;
        // }
        // }
        return field;
    }
}
