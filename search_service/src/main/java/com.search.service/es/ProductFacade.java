package com.search.service.es;

public interface ProductFacade {

    String BEAN_ID = "productFacede";

    String BEAN_TYPE = "product";

    String highlightedFields[] = {"name.name_ik", "name.name_pinyin",
            "name.name_pinyin_first_letter",
            "name.name_lowercase_keyword_ngram_min_size1"};
}
