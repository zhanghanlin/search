package com.search.service.bean;

public class Product extends AbstractEntity {

    private static final long serialVersionUID = 2273493373728543624L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
