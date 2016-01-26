package com.search.web.controller;

import com.search.service.bean.Keyword;
import com.search.service.es.bussiness.KeywordFacadeImpl;
import com.search.service.es.job.KeywordJob;
import com.search.web.response.ResponseContent;
import com.search.web.response.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("keyword")
public class KeywordController {

    static final Logger LOG = LoggerFactory.getLogger(KeywordController.class);

    @Resource
    KeywordFacadeImpl keywordFacade;

    @Resource
    KeywordJob keywordData;

    @RequestMapping("refresh/test")
    @ResponseBody
    public ResponseContent<Keyword> refreshTest() {
        try {
            keywordData.refreshTest(100);
            return new ResponseContent<Keyword>(ResponseEnum.OK);
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("refresh/all")
    @ResponseBody
    public ResponseContent<Keyword> refreshAll() {
        try {
            keywordData.refresh();
            return new ResponseContent<Keyword>(ResponseEnum.OK);
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("refresh/{id}")
    @ResponseBody
    public ResponseContent<Keyword> refreshId(@PathVariable Long id) {
        try {
            keywordData.refresh(id);
            return new ResponseContent<Keyword>(ResponseEnum.OK);
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("refresh/section/{begin}_{end}")
    @ResponseBody
    public ResponseContent<Keyword> refreshSection(@PathVariable Long begin, @PathVariable Long end) {
        try {
            keywordData.refresh(begin, end);
            return new ResponseContent<Keyword>(ResponseEnum.OK);
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("get/{id}")
    @ResponseBody
    public ResponseContent<Keyword> get(@PathVariable Long id) {
        try {
            Keyword t = keywordFacade.get(id);
            if (t != null) {
                return new ResponseContent<Keyword>(ResponseEnum.OK, t);
            } else {
                return new ResponseContent<Keyword>(ResponseEnum.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseContent<Keyword>(ResponseEnum.EXCEPTION);
        }
    }

    @RequestMapping("associate/{key}")
    @ResponseBody
    public ResponseContent<List<Keyword>> associate(@PathVariable String key) {
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
