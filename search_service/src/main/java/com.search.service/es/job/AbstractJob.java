package com.search.service.es.job;

import com.alibaba.fastjson.JSON;
import com.search.service.es.utils.Jerseys;
import com.search.utils.Constants;
import com.sun.jersey.api.client.WebResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractJob<T> {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractJob.class);

    /**
     * Elsticsearch Client
     */
    final WebResource client = Jerseys.createClient(Constants.BASE_URL);

    /**
     * 线程锁
     */
    final Lock lock = new ReentrantLock();

    /**
     * 线程池默认同CPU核数加一
     */
    final ExecutorService exec = Executors.newFixedThreadPool(Runtime
            .getRuntime().availableProcessors() + 1);

    /**
     * 写入业务逻辑
     *
     * @param t Bean
     * @throws Exception
     */
    protected abstract void businessPut(T t) throws Exception;

    /**
     * 将数据写入Es
     * @param id 对象Id
     * @param t 对象
     * @throws Exception
     */
    protected void put(Long id, T t) throws Exception {
        WebResource wr = client.path("/" + Constants.GLOBAL_INDEX_NAME + "/"
                + beanType() + "/" + id);
        String pjson = JSON.toJSON(t).toString();
        wr.put(pjson);
    }

    /**
     * Bean Name
     *
     * @return
     */
    protected abstract String beanType();

    /**
     * 多线程刷新
     *
     * @param list
     */
    protected void run(List<T> list) throws Exception{
        LOG.info("[Job Flush] begin fresh run..............");
        LOG.info("[Job Flush] list size : {}", list.size());
        // 创建计数器
        final CountDownLatch allLatch = new CountDownLatch(list.size());
        try {
            // 当前对象获得锁
            if (lock.tryLock()) {
                LOG.info("[Job Flush] get lock run for");
                for (final T t : list) {
                    exec.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 具体执行子对象实现方法
                                businessPut(t);
                            } catch (Exception e) {
                                e.printStackTrace();
                                LOG.error("[Job Flush] run() error: {}", e.getMessage(), e);
                            } finally {
                                // 每一个线程执行完毕,调用countDown()方法,直到全部的线程(count个)执行完毕,闭锁打开
                                allLatch.countDown();
                            }
                        }
                    });
                }
                long start = System.nanoTime();
                // 当前线程在这里等待所有的线程执行完毕,最多等待5分钟
                allLatch.await(5L, TimeUnit.MINUTES);
                long end = System.nanoTime();
                LOG.info("[Job Flush] time ： {}", (end - start) / (1000 * 1000) + "milliSecond");
            } else {
                LOG.info("[Job Flush] not get lock");
                throw new Exception("Job not get lock");
            }
        } catch (Exception e) {
            LOG.error("[Job Flush] error: {}", e.getMessage(), e);
            throw e;
        } finally {
            // 释放锁
            lock.unlock();
            LOG.info("[Job Flush] un lock");
        }
        LOG.info("[Job Flush] end fresh run..............");
    }
}
