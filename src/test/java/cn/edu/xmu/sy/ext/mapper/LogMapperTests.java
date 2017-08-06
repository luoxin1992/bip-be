/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.LogDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoxin
 * @version 2017-3-14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogMapperTests {
    private final Logger logger = LoggerFactory.getLogger(LogMapperTests.class);

    @Autowired
    private LogMapper logMapper;

    @Test
    public void testSave() {
        LogDO domain = new LogDO();
        domain.setType("类型");
        domain.setContent("内容");
        Assert.assertTrue(logMapper.save(domain) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<LogDO> domains = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LogDO domain = new LogDO();
            domain.setType("类型_" + i);
            domain.setContent("内容_" + i);
            domains.add(domain);
        }
        Assert.assertTrue(logMapper.saveBatch(domains) == domains.size());
    }

    @Test
    public void testUpdateById() {
        LogDO domain = new LogDO();
        domain.setId(78805323623694340L);
        domain.setType("类型_修改");
        domain.setContent("类型_修改");
        Assert.assertTrue(logMapper.updateById(domain) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(logMapper.removeById(78805321618817028L) == 1);
    }

    @Test
    public void testGetById() {
        LogDO domain1 = logMapper.getById(78805323623694340L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        LogDO domain2 = logMapper.getById(78805321618817028L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }
}
