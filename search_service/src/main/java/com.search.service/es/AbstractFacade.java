package com.search.service.es;

import com.search.utils.Constants;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFacade<T> {

    @Autowired
    private ElasticsearchNodeFactoryBean esNode;

    protected Client getClient() {
        try {
            Client c = esNode.getObject().client();
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected SearchRequestBuilder getBuilder() {
        return getClient().prepareSearch(Constants.GLOBAL_INDEX_NAME);
    }

    public abstract T get(Long id);
}
