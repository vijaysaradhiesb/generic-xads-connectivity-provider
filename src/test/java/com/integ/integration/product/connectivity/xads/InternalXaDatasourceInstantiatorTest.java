package com.integ.integration.product.connectivity.xads;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/private-tests/test-context.xml",
        "classpath:/test-connectivity/connection-h2-xads1.xml"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class InternalXaDatasourceInstantiatorTest extends Assert {

    @Autowired
    private DataSource dataSource;

    @Test
    public void xaPropertiesResolverTest() throws SQLException {
        //If this line haven't thrown an exception, it means we already succeed connecting to test DB.
        dataSource.getConnection();
    }
}
