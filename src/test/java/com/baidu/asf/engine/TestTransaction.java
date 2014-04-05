package com.baidu.asf.engine;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-asf3.xml")
public class TestTransaction {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private TxService txService1;
    private long maxInstanceId;

    @Before
    public void loadMaxInstanceId() throws Exception {
        this.maxInstanceId = getMaxInstanceId();
    }

    @Test
    public void testCommit() throws Exception {
        txService1.doLogic();
        // transaction will commit

        long maxId = getMaxInstanceId();

        Assert.assertEquals(maxInstanceId + 1, maxId);
    }

    @Test
    public void testRollback() throws Exception {
        try {
            txService1.doLogicWithException();
            Assert.assertTrue(false);
        } catch (Exception e) {
        }
        //transaction will rollback
        long maxId = getMaxInstanceId();
        Assert.assertEquals(maxInstanceId, maxId);
    }

    private long getMaxInstanceId() throws Exception {
        Connection connection = dataSource.getConnection();

        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(ID_) FROM ASF_INSTANCE");
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            throw new SQLException("No result!!");
        }

        long maxId = rs.getLong(1);

        rs.close();
        stmt.close();

        connection.close();

        return maxId;
    }
}
