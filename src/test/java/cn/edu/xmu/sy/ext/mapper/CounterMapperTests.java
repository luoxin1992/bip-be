/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.CounterDO;
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
public class CounterMapperTests {
    private final Logger logger = LoggerFactory.getLogger(CounterMapperTests.class);

    @Autowired
    private CounterMapper counterMapper;

    @Test
    public void testSave() {
        CounterDO domain = new CounterDO();
        domain.setNumber("编号");
        domain.setName("名称");
        domain.setMac("MAC地址");
        domain.setIp("IP地址");
        Assert.assertTrue(counterMapper.save(domain) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<CounterDO> domains = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CounterDO domain = new CounterDO();
            domain.setNumber("编号_" + i);
            domain.setName("名称_" + i);
            domain.setMac("MAC地址_" + i);
            domain.setIp("IP地址_" + i);
            domains.add(domain);
        }
        Assert.assertTrue(counterMapper.saveBatch(domains) == domains.size());
    }

    @Test
    public void testUpdateById() {
        CounterDO domain = new CounterDO();
        domain.setId(78825709010681860L);
        domain.setNumber("编号_修改");
        domain.setName("名称_修改");
        domain.setMac("MAC地址_修改");
        domain.setIp("IP地址_修改");
        Assert.assertTrue(counterMapper.updateById(domain) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(counterMapper.removeById(78825706632511490L) == 1);
    }

    @Test
    public void testGetById() {
        CounterDO domain1 = counterMapper.getById(78825709010681860L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        CounterDO domain2 = counterMapper.getById(78825706632511490L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }

    @Test
    public void testGetIdByNumber() {
        CounterDO domain = counterMapper.getByNumber("编号_6");
        logger.info("testGetById --> {}", domain);
        Assert.assertEquals(domain.getId().longValue(), 78825709014876162L);
    }

    @Test
    public void testGetIdByMacAndIp() {
        CounterDO domain = counterMapper.getByMacAndIp("MAC地址_7", "IP地址_7");
        logger.info("testGetIdByMacAndIp --> {}", domain);
        Assert.assertEquals(domain.getId().longValue(), 78825709014876163L);
    }

    @Test
    public void testListById() {
        List<Long> ids = new ArrayList<>();
        ids.add(78825709014876162L);
        ids.add(78825709014876163L);

        List<CounterDO> domains = counterMapper.listById(ids);
        logger.info("testListById --> {}", domains.size());
        Assert.assertEquals(domains.size(), 2);
    }

    @Test
    public void testCountAll() {
        Assert.assertEquals(counterMapper.countAll().longValue(), 10);
    }

    @Test
    public void testListAll() {
        List<CounterDO> domains = counterMapper.listAll(0L, 100);
        logger.info("testListAll --> {}", domains.size());
        Assert.assertEquals(domains.size(), 10);
    }
}
