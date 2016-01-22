package com.search.service.es;

import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

public class ElasticsearchNodeFactoryBean implements FactoryBean<Node>, InitializingBean, DisposableBean {

    protected final static Logger LOG = LoggerFactory.getLogger(ElasticsearchNodeFactoryBean.class);

    protected List<Resource> configLocations;

    private Resource configLocation;

    private Map<String, String> settings;

    private Node node;

    public void setConfigLocations(List<Resource> configLocations) {
        this.configLocations = configLocations;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        internalCreateNode();
    }

    private void internalCreateNode() {
        final NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder();
        if (null != configLocation) {
            internalLoadSettings(nodeBuilder, configLocation);
        }
        if (null != settings) {
            nodeBuilder.getSettings().put(settings);
        }
        node = nodeBuilder.node();
    }

    private void internalLoadSettings(final NodeBuilder nodeBuilder, final Resource configLocation) {
        try {
            final String fileName = configLocation.getFilename();
            if (LOG.isInfoEnabled()) {
                LOG.info("Loading configuration file from: {} ", fileName);
            }
            nodeBuilder.getSettings().loadFromStream(fileName, configLocation.getInputStream());
        } catch (final Exception e) {
            throw new IllegalArgumentException("Could not load settings from configLocation: " + configLocation.getDescription(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        try {
            node.close();
        } catch (final Exception e) {
            LOG.error("Error closing Elasticsearch node", e);
        }
    }

    @Override
    public Node getObject() throws Exception {
        return node;
    }

    @Override
    public Class<Node> getObjectType() {
        return Node.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}