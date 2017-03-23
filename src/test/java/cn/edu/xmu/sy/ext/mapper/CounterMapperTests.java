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

import java.time.LocalDateTime;
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
        CounterDO counterDO = new CounterDO();
        counterDO.setNumber("00");
        counterDO.setName("测试");
        counterDO.setMac("000000000000");
        counterDO.setIp("0.0.0.0");
        counterDO.setGmtCreate(LocalDateTime.now());
        counterDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(counterMapper.save(counterDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<CounterDO> counterDOs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CounterDO counterDO = new CounterDO();
            counterDO.setNumber("0" + i);
            counterDO.setName("测试柜台" + i);
            counterDO.setMac("00000000000" + i);
            counterDO.setIp("192.168.1.10" + i);
            counterDO.setGmtCreate(LocalDateTime.now());
            counterDO.setGmtModify(LocalDateTime.now());
            counterDOs.add(counterDO);
        }
        Assert.assertTrue(counterMapper.saveBatch(counterDOs) == counterDOs.size());
    }

    @Test
    public void testUpdateById() {
        CounterDO counterDO = new CounterDO();
        counterDO.setId(26223598501888L);
        counterDO.setNumber("00_修改");
        counterDO.setName("测试_修改");
        counterDO.setMac("FFFFFFFFFFFF");
        counterDO.setIp("255.255.255.255");
        counterDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(counterMapper.updateById(counterDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(counterMapper.removeById(26223598501888L) == 1);
    }

    @Test
    public void testGetById() {
        logger.info("{}", counterMapper.getById(26223598501888L));
    }
}
