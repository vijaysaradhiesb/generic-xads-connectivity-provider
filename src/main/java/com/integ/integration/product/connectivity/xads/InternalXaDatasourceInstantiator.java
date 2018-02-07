package com.integ.integration.product.connectivity.xads;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;

import javax.sql.XADataSource;
import java.util.*;

public class InternalXaDatasourceInstantiator implements FactoryBean<XADataSource>, ApplicationContextAware {
    private final String driverClassName;
    private AbstractApplicationContext applicationContext;
    private Properties givenProperties;
    private Map<String, String> resolvedProperties;
    private final static List<String> osgiBlackList;

    static {
        osgiBlackList = new ArrayList<String>();
        osgiBlackList.add("service.pid");
        osgiBlackList.add("felix.fileinstall.filename");
    }

    public InternalXaDatasourceInstantiator(String driverClassName, Properties properties) {
        this.driverClassName = driverClassName;
        this.givenProperties = properties;
    }

    @Override
    public XADataSource getObject() throws Exception {
        Class<?> clazz = Class.forName(driverClassName);
        XADataSource xaDataSource = (XADataSource) clazz.newInstance();
        BeanUtils.populate(xaDataSource, getResolvedProperties()); // BeanUtils populate method arguments changed in new version and we updated the code accordingly.

        return xaDataSource;
    }

    private Map<String, String> getResolvedProperties() throws Exception {
        if (resolvedProperties == null) {
            resolvedProperties = new HashMap<>();

            for (Map.Entry<Object, Object> entry : givenProperties.entrySet()) {
                if (!osgiBlackList.contains(entry.getKey().toString())) {
                    String key = applicationContext.getBeanFactory().resolveEmbeddedValue((String) entry.getKey());
                    String value = applicationContext.getBeanFactory().resolveEmbeddedValue((String) entry.getValue());

                    resolvedProperties.put(key, value);
                }
            }
        }

        return resolvedProperties;
    }

    @Override
    public Class<?> getObjectType() {
        return Properties.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (AbstractApplicationContext) applicationContext;
    }
}
