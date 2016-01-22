package com.search.service.es.bussiness;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class SearchResult<T> {

    private int totalHits;

    private List<T> items;

    public int getTotalHits() {
        return totalHits;
    }

    public void setTotalHits(int totalHits) {
        this.totalHits = totalHits;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}