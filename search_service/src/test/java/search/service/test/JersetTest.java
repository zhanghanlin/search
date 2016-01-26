package search.service.test;

import com.alibaba.fastjson.JSON;
import com.search.service.bean.Keyword;
import com.search.service.es.bussiness.SearchResult;
import com.search.utils.Constants;
import com.search.utils.StringUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/25.
 */
public class JersetTest {
    /**
     * settings
     */
    private static Settings defaultSettings = ImmutableSettings
            .settingsBuilder().put("cluster.name", Constants.GLOBAL_INDEX_NAME)
            .put("discovery.zen.ping.multicast.enabled", true).build();

    /**
     * create node
     */
    final static Node node = NodeBuilder.nodeBuilder().client(true)
            .settings(defaultSettings).build();

    static Settings settings = ImmutableSettings.settingsBuilder()
            .put("cluster.name", Constants.GLOBAL_INDEX_NAME).build();

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Keyword> list = search("民", "", SortOrder.ASC, 1, 10).getItems();
        System.out.println("total:" + list.size());
        for (Keyword keyword : list) {
            System.out.println("keyword:" + keyword.toJSON());
        }
    }

    static String highlightedFields[] = {"word.word_ik", "word.word_pinyin",
            "word.word_pinyin_first_letter",
            "word.word_lowercase_keyword_ngram_min_size1"};

    public static SearchResult<Keyword> search(String key, String sort, SortOrder order, int pageNo, int pageSize) {
        final SearchResult<Keyword> searchResult = new SearchResult<Keyword>();
        Client client = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(
                        "127.0.0.1", 9300));
        SearchRequestBuilder srb = client.prepareSearch(
                Constants.GLOBAL_INDEX_NAME).setTypes("keyword");

        BoolQueryBuilder bool = new BoolQueryBuilder();
        MultiMatchQueryBuilder builder = QueryBuilders.multiMatchQuery(key,
                highlightedFields).operator(MatchQueryBuilder.Operator.AND);
        bool.must(builder);
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
        System.out.println(srb.toString());
        SearchResponse searchResponse = srb.execute().actionGet();
        client.close();
        final SearchHits hits = searchResponse.getHits();
        System.out.println("serarch hits : " + hits.getTotalHits());
        List<Keyword> items = new ArrayList<Keyword>();
        for (final SearchHit searchHit : hits.getHits()) {
            final Keyword keyword = JSON.parseObject(
                    searchHit.getSourceAsString(), Keyword.class);
            // 获取对应的高亮域
            Map<String, HighlightField> hig = searchHit.highlightFields();
            // 从设定的高亮域中取得指定域
            HighlightField nameField = getHighlightField(hig);
            if (nameField != null) {
                // 取得定义的高亮标签
                Text[] nameTexts = nameField.fragments();
                String word = "";
                // 为name串值增加自定义的高亮标签
                for (Text text : nameTexts) {
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
     * HighlightField 高亮
     *
     * @param hig
     * @return
     */
    private static HighlightField getHighlightField(Map<String, HighlightField> hig) {
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
