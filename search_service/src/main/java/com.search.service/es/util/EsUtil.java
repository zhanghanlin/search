package com.search.service.es.util;

import com.search.service.es.ElasticsearchNodeFactoryBean;
import com.search.utils.Constants;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsUtil {

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
}
