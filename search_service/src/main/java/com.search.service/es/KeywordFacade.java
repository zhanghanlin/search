package com.search.service.es;

public interface KeywordFacade {

    String BEAN_ID = "keywordFacade";

    String BEAN_TYPE = "keyword";

    String highlightedFields[] = {"name.name_ik", "name.name_pinyin",
            "name.name_pinyin_first_letter",
            "name.name_lowercase_keyword_ngram_min_size1"};
}
