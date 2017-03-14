/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.com.lx1992.lib.util.IDGenerator;
import cn.edu.xmu.sy.ext.domain.LogDO;
import cn.edu.xmu.sy.ext.meta.EntityEnum;
import cn.edu.xmu.sy.ext.meta.WorkerEnum;
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
public class LogMapperTests {
    private final Logger logger = LoggerFactory.getLogger(LogMapperTests.class);

    @Autowired
    private LogMapper logMapper;

    @Test
    public void testSave() {
        LogDO logDO = new LogDO();
        logDO.setId(IDGenerator.nextId(EntityEnum.RESOURCE.getCode(), WorkerEnum.DEFAULT.getCode()));
        logDO.setPlatform("测试");
        logDO.setVersion("测试");
        logDO.setPrimaryCategory("测试");
        logDO.setSecondaryCategory("测试");
        logDO.setContent("测试");
        logDO.setGmtCreate(LocalDateTime.now());
        logDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(logMapper.save(logDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<LogDO> logDOs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LogDO logDO = new LogDO();
            logDO.setId(IDGenerator.nextId(EntityEnum.LOG.getCode(), WorkerEnum.DEFAULT.getCode()));
            logDO.setPlatform("测试平台" + i);
            logDO.setVersion("测试版本" + i);
            logDO.setPrimaryCategory("测试一级类别" + i);
            logDO.setSecondaryCategory("测试二级类别" + i);
            logDO.setContent("测试内容" + i);
            logDO.setGmtCreate(LocalDateTime.now());
            logDO.setGmtModify(LocalDateTime.now());
            logDOs.add(logDO);
        }
        Assert.assertTrue(logMapper.saveBatch(logDOs) == logDOs.size());
    }

    @Test
    public void testUpdateById() {
        LogDO logDO = new LogDO();
        logDO.setId(26284131221509L);
        logDO.setPlatform("测试_修改");
        logDO.setVersion("测试_修改");
        logDO.setPrimaryCategory("测试_修改");
        logDO.setSecondaryCategory("测试_修改");
        logDO.setContent("测试_修改");
        logDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(logMapper.updateById(logDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(logMapper.removeById(26284131221509L) == 1);
    }

    @Test
    public void testGetById() {
        logger.info("{}", logMapper.getById(26284131221509L));
    }
}
