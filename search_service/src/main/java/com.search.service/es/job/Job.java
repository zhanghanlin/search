package com.search.service.es.job;

import com.search.service.es.util.EsUtil;
import com.search.utils.Constants;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.common.io.Streams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class Job {

    @Resource
    EsUtil esUtil;

    @Resource
    KeywordData keywordData;

    /**
     * 刷新全部
     */
    public void flushAll() throws Exception{
        flushKeyword(null);
    }

    /**
     * 刷新商品
     *
     * @param list
     */
    public void flushKeyword(List<String> list) throws Exception{
        init(keywordData.beanType());
        keywordData.run(list);
    }


    /**
     * 初始化类型
     */
    private void init(String type) {
        try {
            String mapping = Streams.copyToStringFromClasspath(Constants.ES_SEARCH_JSON_PATH + type + ".json");
            try {
                @SuppressWarnings("unused")
                PutMappingResponse mappingResponse = esUtil.getClient().admin()
                        .indices()
                        .preparePutMapping(Constants.GLOBAL_INDEX_NAME)
                        .setType(type).setSource(mapping).execute().actionGet();
            } catch (ElasticsearchException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
