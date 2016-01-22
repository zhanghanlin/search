package com.search.service.job;

import com.search.service.bean.Product;
import com.search.service.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductData extends AbstractData<Product> {

    @Autowired
    ProductDao productDao;

    List<Product> list = new ArrayList<Product>();

    @Override
    protected void businessPut(Product product) throws Exception {
        put(product.getId(), product);
    }

    @Override
    protected List<Product> getList(List<String> param) {
        if (null != param && param.size() > 0) {
            list = productDao.searchByIds(param);
        } else {
            list = productDao.searchAll();
        }
        return list;
    }

    @Override
    protected String beanType() {
        return "product";
    }
}
