package com.search.service.dao;

import com.search.service.bean.Keyword;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class KeywordDao extends SqlSessionDaoSupport {

    public Keyword get(Long id) {
        return this.getSqlSession().selectOne("Keyword.searchById", id);
    }

    public List<Keyword> searchAll() {
        return this.getSqlSession().selectList("Keyword.searchAll");
    }

    public List<Keyword> searchBySection(Long begin,Long end) {
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("begin",begin);
        map.put("end",end);
        return this.getSqlSession().selectList("Keyword.searchBySection", map);
    }
}