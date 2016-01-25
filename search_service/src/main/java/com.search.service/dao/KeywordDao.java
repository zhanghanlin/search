package com.search.service.dao;

import com.search.service.bean.Keyword;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KeywordDao extends SqlSessionDaoSupport {

    public Keyword get(Long id) {
        return this.getSqlSession().selectOne("Keyword.searchById", id);
    }

    public List<Keyword> searchAll() {
        return this.getSqlSession().selectList("Keyword.searchAll");
    }

    public List<Keyword> searchByIds(List<String> param) {
        return this.getSqlSession().selectList("Keyword.searchByIds", param);
    }
}