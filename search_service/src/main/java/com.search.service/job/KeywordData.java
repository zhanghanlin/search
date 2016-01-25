package com.search.service.job;

import com.search.service.bean.Keyword;
import com.search.service.dao.KeywordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeywordData extends AbstractData<Keyword> {

    @Autowired
    KeywordDao keywordDao;

    List<Keyword> list = new ArrayList<Keyword>();

    @Override
    protected void businessPut(Keyword keyword) throws Exception {
        put(keyword.getId(), keyword);
    }

    @Override
    protected List<Keyword> getList(List<String> param) {
        if (null != param && param.size() > 0) {
            list = keywordDao.searchByIds(param);
        } else {
            list = keywordDao.searchAll();
        }
        return list;
    }

    @Override
    protected String beanType() {
        return "keyword";
    }
}
