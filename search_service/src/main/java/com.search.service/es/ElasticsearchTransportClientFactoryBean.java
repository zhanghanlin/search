package com.search.service.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Map.Entry;

public class ElasticsearchTransportClientFactoryBean implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    protected final static Logger LOG = LoggerFactory.getLogger(ElasticsearchTransportClientFactoryBean.class);

    private TransportClient transportClient;

    private Map<String, Integer> transportAddresses;

    public void setTransportAddresses(final Map<String, Integer> transportAddresses) {
        this.transportAddresses = transportAddresses;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        internalCreateTransportClient();
    }

    private void internalCreateTransportClient() {
        final TransportClient client = new TransportClient();
        if (null != transportAddresses) {
            for (final Entry<String, Integer> address : transportAddresses.entrySet()) {
                if (LOG.isInfoEnabled()) {
                    LOG.info("Adding transport address: {} , port: {}", address.getKey(), address.getValue());
                }
                client.addTransportAddress(new InetSocketTransportAddress(address.getKey(), address.getValue()));
            }
        }
        transportClient = client;
    }

    @Override
    public void destroy() throws Exception {
        transportClient.close();
    }

    @Override
    public TransportClient getObject() throws Exception {
        return transportClient;
    }

    @Override
    public Class<TransportClient> getObjectType() {
        return TransportClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}