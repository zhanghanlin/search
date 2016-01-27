package com.search.service.dao;

import com.search.AbstractTest;
import com.search.service.bean.Keyword;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class KeywordDaoTest extends AbstractTest {

    @Resource
    KeywordDao keywordDao;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: get(Long id)
     */
    @Test
    public void testGet() throws Exception {
        Keyword keyword = keywordDao.get(100000L);
        LOG.info(keyword.toString());
    }

    /**
     * Method: searchAll()
     */
    @Test
    public void testSearchAll() throws Exception {
    }

    /**
     * Method: searchBySection(Long begin, Long end)
     */
    @Test
    public void testSearchBySection() throws Exception {
        Long begin = 100000L;
        Long end = 100003L;
        List<Keyword> list = keywordDao.searchBySection(begin, end);
        LOG.info("Begin : {},End : {},Size : {}", begin, end, list.size());
    }
} 
