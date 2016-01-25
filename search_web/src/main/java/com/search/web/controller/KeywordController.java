package com.search.web.controller;

import com.search.service.bean.Keyword;
import com.search.service.es.bussiness.KeywordFacadeImpl;
import com.search.service.es.job.Job;
import com.search.web.response.ResponseContent;
import com.search.web.response.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("keyword")
public class KeywordController {

    static final Logger LOG = LoggerFactory.getLogger(KeywordController.class);

    @Resource
    KeywordFacadeImpl keywordFacade;

    @Resource
    Job job;

    @RequestMapping("testInit")
    public ResponseContent<Keyword> testInit() {
        try {
            keywordFacade.testInit(100);
            return new ResponseContent<Keyword>(ResponseEnum.OK);
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("init")
    public ResponseContent<Keyword> init(List<String> params) {
        try {
            job.flushKeyword(params);
            return new ResponseContent<Keyword>(ResponseEnum.OK);
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("get")
    public ResponseContent<Keyword> get(Long id) {
        try {
            Keyword t = keywordFacade.get(id);
            if (t != null) {
                return new ResponseContent<Keyword>(ResponseEnum.OK);
            } else {
                return new ResponseContent<Keyword>(ResponseEnum.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("associate")
    public ResponseContent<List<Keyword>> associate(String key) {
        try {
            List<Keyword> list = keywordFacade.associateWord(key);
            if (list != null && !list.isEmpty()) {
                return new ResponseContent<List<Keyword>>(ResponseEnum.OK, list);
            } else {
                return new ResponseContent<List<Keyword>>(ResponseEnum.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseContent<List<Keyword>>(ResponseEnum.EXCEPTION);
        }
    }
}
