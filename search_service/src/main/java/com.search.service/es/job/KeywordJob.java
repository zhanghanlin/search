package com.search.service.es.job;

import com.alibaba.fastjson.JSON;
import com.search.service.bean.Keyword;
import com.search.service.dao.KeywordDao;
import com.search.service.es.util.EsUtil;
import com.search.service.es.util.Jerseys;
import com.search.utils.Constants;
import com.search.utils.random.poetry.PoemUtils;
import com.search.utils.string.StringUtils;
import com.sun.jersey.api.client.WebResource;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.common.io.Streams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class KeywordJob extends AbstractJob<Keyword> {


    @Resource
    KeywordDao keywordDao;

    @Resource
    EsUtil esUtil;

    private final static String initType = "keyword";

    @Override
    protected void businessPut(Keyword keyword) throws Exception {
        put(keyword.getId(), keyword);
    }

    /**
     * 刷新所有商品
     */
    public void refresh() throws Exception {
        esUtil.init(initType);
        List<Keyword> list = keywordDao.searchAll();
        run(list);
    }

    /**
     * 刷新单个商品
     *
     * @param id
     */
    public void refresh(Long id) throws Exception {
        esUtil.init(initType);
        Keyword keyword = keywordDao.get(id);
        businessPut(keyword);
    }

    /**
     * 根据Id段刷新商品
     *
     * @param begin
     * @param end
     * @throws Exception
     */
    public void refresh(Long begin, Long end) throws Exception {
        esUtil.init(initType);
        List<Keyword> list = keywordDao.searchBySection(begin, end);
        run(list);
    }

    /**
     * 初始化数据-随机字符串
     *
     * @param count
     */
    public void refreshTest(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            try {
                Keyword keyword = new Keyword();
                int id = StringUtils.randomInt(10000, 99990);
                keyword.setId(Long.valueOf(id));
                keyword.setWord(PoemUtils.getPoemSentence(5));
                businessPut(keyword);
            } catch (Exception e) {
                throw e;
            }
        }
    }

    @Override
    protected String beanType() {
        return initType;
    }
}