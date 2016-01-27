package com.search.service.es.job;

import com.search.service.bean.Keyword;
import com.search.service.dao.KeywordDao;
import com.search.service.es.utils.EsUtils;
import com.search.utils.random.poetry.PoemUtils;
import com.search.utils.string.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class KeywordJob extends AbstractJob<Keyword> {

    @Resource
    KeywordDao keywordDao;

    private final static String initType = "keyword";

    @Override
    protected void businessPut(Keyword keyword) throws Exception {
        this.put(keyword.getId(), keyword);
    }

    /**
     * 刷新所有商品
     */
    public Integer refresh() throws Exception {
        List<Keyword> list = keywordDao.searchAll();
        run(list);
        return list.size();
    }

    /**
     * 刷新单个商品
     *
     * @param id
     */
    public Keyword refresh(Long id) throws Exception {
        this.esUtils.init(initType);
        Keyword keyword = keywordDao.get(id);
        businessPut(keyword);
        return keyword;
    }

    /**
     * 根据Id段刷新商品
     *
     * @param begin
     * @param end
     * @throws Exception
     */
    public Integer refresh(Long begin, Long end) throws Exception {
        List<Keyword> list = keywordDao.searchBySection(begin, end);
        run(list);
        return list.size();
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