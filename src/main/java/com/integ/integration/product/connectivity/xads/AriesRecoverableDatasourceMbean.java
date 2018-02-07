package com.integ.integration.product.connectivity.xads;


import org.apache.geronimo.connector.outbound.GenericConnectionManager;
import org.apache.geronimo.connector.outbound.PoolingAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

public class AriesRecoverableDatasourceMbean implements PoolingAttributes {
    private GenericConnectionManager genericConnectionManager;
    private static final Logger LOG = LoggerFactory.getLogger(AriesRecoverableDatasourceMbean.class);

    public AriesRecoverableDatasourceMbean(DataSource dataSource, String datasourceName) throws NoSuchFieldException, IllegalAccessException, SQLException {
        Connection con = null;
        try {
            con = dataSource.getConnection();
            Field cmField = con.getClass().getDeclaredField("cm");
            cmField.setAccessible(true);
            genericConnectionManager = (GenericConnectionManager) cmField.get(con);
        } catch (Exception e) {
            LOG.warn("Datasource MBean not available for datasource name: " + datasourceName, e);
        } finally {
            try { con.close(); } catch (Exception e) {}
        }
    }


    @Override
    public int getPartitionCount() {
        return genericConnectionManager.getPartitionCount();
    }

    public int getConnectionCount() {
        return genericConnectionManager.getConnectionCount();
    }

    @Override
    public int getIdleConnectionCount() {
        return genericConnectionManager.getIdleConnectionCount();
    }

    @Override
    public int getPartitionMaxSize() {
        return genericConnectionManager.getPartitionMaxSize();
    }

    @Override
    public void setPartitionMaxSize(int maxSize) throws InterruptedException {
        genericConnectionManager.setPartitionMaxSize(maxSize);
    }

    @Override
    public int getPartitionMinSize() {
        return genericConnectionManager.getPartitionMinSize();
    }

    @Override
    public void setPartitionMinSize(int minSize) {
        genericConnectionManager.setPartitionMinSize(minSize);
    }

    @Override
    public int getBlockingTimeoutMilliseconds() {
        return genericConnectionManager.getBlockingTimeoutMilliseconds();
    }

    @Override
    public void setBlockingTimeoutMilliseconds(int timeoutMilliseconds) {
        genericConnectionManager.setBlockingTimeoutMilliseconds(timeoutMilliseconds);
    }

    @Override
    public int getIdleTimeoutMinutes() {
        return genericConnectionManager.getIdleTimeoutMinutes();
    }

    @Override
    public void setIdleTimeoutMinutes(int idleTimeoutMinutes) {
        genericConnectionManager.setIdleTimeoutMinutes(idleTimeoutMinutes);
    }

    public boolean isValidating() throws NoSuchFieldException {
        return genericConnectionManager.getClass().getDeclaredField("validatingInterval") != null;
    }

    public long getValidatingInterval() throws NoSuchFieldException, IllegalAccessException {
        if (isValidating()) {
            Field validatingIntervalField = genericConnectionManager.getClass().getDeclaredField("validatingInterval");
            validatingIntervalField.setAccessible(true);
            return (long) validatingIntervalField.get(genericConnectionManager);
        }

        return -1L;
    }
}
