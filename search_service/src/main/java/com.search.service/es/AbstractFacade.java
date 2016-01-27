package com.search.service.es;

import com.search.service.es.utils.EsUtils;

import javax.annotation.Resource;

public abstract class AbstractFacade<T> {

    @Resource
    protected EsUtils esUtils;

    /**
     * 根据Id获取单个对象
     * @param id 对象ID
     * @return T
     */
    public abstract T get(Long id);
}
