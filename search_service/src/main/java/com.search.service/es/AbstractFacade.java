package com.search.service.es;

import com.search.utils.Constants;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFacade<T> {
    public abstract T get(Long id);
}
