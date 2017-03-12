/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.com.lx1992.lib.util.IDGenerator;
import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
 * @version 2017-3-12
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FingerprintMapperTests {
    private final Logger logger = LoggerFactory.getLogger(FingerprintMapperTests.class);

    @Autowired
    private FingerprintMapper fingerprintMapper;

    @Test
    public void testSave() {
        FingerprintDO fingerprintDO = new FingerprintDO();
        fingerprintDO.setId(IDGenerator.nextId(0, 0));
        fingerprintDO.setUserId(25494732931075L);
        fingerprintDO.setFinger("测试");
        fingerprintDO.setTemplate("AABBCCDD");
        fingerprintDO.setGmtCreate(LocalDateTime.now());
        fingerprintDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(fingerprintMapper.save(fingerprintDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<FingerprintDO> fingerprintDOs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FingerprintDO fingerprintDO = new FingerprintDO();
            fingerprintDO.setId(IDGenerator.nextId(0, 0));
            fingerprintDO.setUserId(25494732931075L + i);
            fingerprintDO.setFinger("测试" + i);
            fingerprintDO.setTemplate("AABBCCDD" + i);
            fingerprintDO.setGmtCreate(LocalDateTime.now());
            fingerprintDO.setGmtModify(LocalDateTime.now());
            fingerprintDOs.add(fingerprintDO);
        }
        Assert.assertTrue(fingerprintMapper.saveBatch(fingerprintDOs) == fingerprintDOs.size());
    }

    @Test
    public void testUpdateById() {
        FingerprintDO fingerprintDO = new FingerprintDO();
        fingerprintDO.setId(25503054430209L);
        fingerprintDO.setUserId(0L);
        fingerprintDO.setFinger("测试_修改");
        fingerprintDO.setTemplate("AABBCCDD_ABCD");
        fingerprintDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(fingerprintMapper.updateById(fingerprintDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(fingerprintMapper.removeById(25503054430209L) == 1);
    }

    @Test
    public void testRemoveByUserId() {
        Assert.assertTrue(fingerprintMapper.removeByUserId(25494732931075L) == 2);
    }

    @Test
    public void testGetById() {
        logger.info("{}", ToStringBuilder.reflectionToString(fingerprintMapper.getById(25504769900545L)));
    }

    @Test
    public void testGetByUserId() {
        List<FingerprintQueryResult> results = fingerprintMapper.getByUserId(25494732931075L);
        for (FingerprintQueryResult result : results) {
            logger.info("{}", ToStringBuilder.reflectionToString(result));
        }
    }

    @Test
    public void testListByUserId() {
        List<Long> userIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userIds.add(25494732931076L + i);
        }
        List<FingerprintQueryResult> results = fingerprintMapper.listByUserId(userIds);
        for (FingerprintQueryResult result : results) {
            logger.info("{}", ToStringBuilder.reflectionToString(result));
        }
    }
}
