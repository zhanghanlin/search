package com.search.service.es.bussiness;

import com.alibaba.fastjson.JSON;
import com.search.service.bean.Product;
import com.search.service.es.AbstractFacade;
import com.search.service.es.ProductFacade;
import com.search.utils.Constants;
import com.search.utils.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductFacadeImpl extends AbstractFacade<Product> implements ProductFacade {

    protected final static Logger LOG = LoggerFactory.getLogger(ProductFacadeImpl.class);

    @Override
    public Product get(Long id) {
        Product product = null;
        GetResponse gp = getClient().prepareGet(Constants.GLOBAL_INDEX_NAME, BEAN_TYPE, id.toString()).execute().actionGet();
        if (gp.isExists()) {
            product = JSON.parseObject(gp.getSourceAsString(), Product.class);
        } else {
            LOG.info("Not Exists : {}", id);
        }
        return product;
    }

    public SearchResult<Product> search(String key, String sort, SortOrder order, int pageNo, int pageSize) {
        final SearchResult<Product> searchResult = new SearchResult<Product>();
        SearchRequestBuilder srb = builder(key, sort, order, pageNo, pageSize);
        SearchResponse searchResponse = srb.execute().actionGet();
        getClient().close();
        final SearchHits hits = searchResponse.getHits();
        List<Product> items = new ArrayList<Product>();
        for (final SearchHit searchHit : hits.getHits()) {
            final Product product = JSON.parseObject(
                    searchHit.getSourceAsString(), Product.class);
            // 获取对应的高亮域
            Map<String, HighlightField> hig = searchHit.highlightFields();
            // 从设定的高亮域中取得指定域
            HighlightField nameField = getHighlightField(hig);
            if (nameField != null) {
                // 取得定义的高亮标签
                Text[] nameTexts = nameField.fragments();
                String name = "";
                // 为name串值增加自定义的高亮标签
                for (Text text : nameTexts) {
                    name += text;
                }
                // 将追加了高亮标签的串值重新填充到对应的对象
                product.setName(name);
            }
            items.add(product);
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
     * @return SearchResult<Product>
     */
    private SearchRequestBuilder builder(String key, String sort,
                                         SortOrder order, int pageNo, int pageSize) {
        BoolQueryBuilder bool = new BoolQueryBuilder();
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery(key,
                highlightedFields).operator(MatchQueryBuilder.Operator.AND);
        bool.must(builder);
        SearchRequestBuilder srb = getBuilder().setTypes(BEAN_TYPE);
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
