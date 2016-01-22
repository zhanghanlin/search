package com.search.service.dao;

import com.search.service.bean.Product;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDao extends SqlSessionDaoSupport {

    public Product get(Long id) {
        return this.getSqlSession().selectOne("Product.searchById", id);
    }

    public List<Product> searchAll() {
        return this.getSqlSession().selectList("Product.searchAll");
    }

    public List<Product> searchByIds(List<String> param) {
        return this.getSqlSession().selectList("Product.searchByIds", param);
    }
}