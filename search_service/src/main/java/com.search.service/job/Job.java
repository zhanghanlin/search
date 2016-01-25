package com.search.service.job;

import com.search.service.es.ElasticsearchNodeFactoryBean;
import com.search.utils.Constants;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.io.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class Job {

    @Autowired
    ElasticsearchNodeFactoryBean esNode;

    private Client esClient;

    public Client getEsClient() {
        if (esClient == null) {
            try {
                esClient = esNode.getObject().client();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return esClient;
    }

    @Autowired
    KeywordData keywordData;

    /**
     * 刷新全部
     */
    public void flushAll() {
        flushKeyword(null);
    }

    /**
     * 刷新商品
     *
     * @param list
     */
    public void flushKeyword(List<String> list) {
        init(keywordData.beanType());
        keywordData.run(list);
    }


    /**
     * 初始化类型
     */
    private void init(String type) {
        try {
            String mapping = Streams.copyToStringFromClasspath(Constants.ES_SEARCH_JSON_PATH + type + ".json");
            try {
                @SuppressWarnings("unused")
                PutMappingResponse mappingResponse = getEsClient().admin()
                        .indices()
                        .preparePutMapping(Constants.GLOBAL_INDEX_NAME)
                        .setType(type).setSource(mapping).execute().actionGet();
            } catch (ElasticsearchException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
