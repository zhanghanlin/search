package com.search.service.es.utils;

import com.search.service.es.ElasticsearchNodeFactoryBean;
import com.search.utils.Constants;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.io.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EsUtils {

    @Autowired
    private ElasticsearchNodeFactoryBean esNode;

    public Client getClient() {
        try {
            Client c = esNode.getObject().client();
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SearchRequestBuilder getBuilder() {
        return getClient().prepareSearch(Constants.GLOBAL_INDEX_NAME);
    }

    /**
     * 初始化类型
     */
    public void init(String type) {
        try {
            String mapping = Streams.copyToStringFromClasspath(Constants.ES_SEARCH_JSON_PATH + type + ".json");
            try {
                @SuppressWarnings("unused")
                PutMappingResponse mappingResponse = getClient().admin()
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
