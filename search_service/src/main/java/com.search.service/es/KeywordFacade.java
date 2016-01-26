package com.search.service.es;

public interface KeywordFacade {

    String BEAN_ID = "keywordFacade";

    String BEAN_TYPE = "keyword";

    String highlightedFields[] = {"word.word_ik", "word.word_pinyin",
            "word.word_pinyin_first_letter",
            "word.word_lowercase_keyword_ngram_min_size1"};
}
