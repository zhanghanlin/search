package com.search.service.es;

public abstract class AbstractFacade<T> {

    public abstract T get(Long id);
}
