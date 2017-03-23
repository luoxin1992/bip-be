/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.FingerprintDO;
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
        fingerprintDO.setUserId(26208150618113L);
        fingerprintDO.setFinger("测试");
        fingerprintDO.setTemplate("");
        fingerprintDO.setEnrollTime(LocalDateTime.of(2017, 1, 13, 15, 23, 39));
        fingerprintDO.setIdentifyTime(LocalDateTime.of(2017, 2, 28, 8, 17, 56));
        fingerprintDO.setGmtCreate(LocalDateTime.now());
        fingerprintDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(fingerprintMapper.save(fingerprintDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<FingerprintDO> fingerprintDOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= 2; j++) {
                FingerprintDO fingerprintDO = new FingerprintDO();
                fingerprintDO.setUserId(26208708460552L + i);
                fingerprintDO.setFinger("测试手指" + j);
                fingerprintDO.setTemplate("");
                fingerprintDO.setEnrollTime(LocalDateTime.now());
                fingerprintDO.setGmtCreate(LocalDateTime.now());
                fingerprintDO.setGmtModify(LocalDateTime.now());
                fingerprintDOs.add(fingerprintDO);
            }
        }
        Assert.assertTrue(fingerprintMapper.saveBatch(fingerprintDOs) == fingerprintDOs.size());
    }

    @Test
    public void testUpdateById() {
        FingerprintDO fingerprintDO = new FingerprintDO();
        fingerprintDO.setId(26209752973319L);
        fingerprintDO.setUserId(0L);
        fingerprintDO.setFinger("测试_修改");
        fingerprintDO.setTemplate("测试_修改");
        fingerprintDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(fingerprintMapper.updateById(fingerprintDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(fingerprintMapper.removeById(26209752973319L) == 1);
    }

    @Test
    public void testRemoveByUserId() {
        Assert.assertTrue(fingerprintMapper.removeByUserId(26208150618113L) == 2);
    }

    @Test
    public void testGetById() {
        logger.info("{}", fingerprintMapper.getById(26211141287942L));
    }

    @Test
    public void testGetByUserId() {
        List<FingerprintDO> results = fingerprintMapper.getByUserId(26208708460552L);
        for (FingerprintDO result : results) {
            logger.info("{}", result);
        }
    }

    @Test
    public void testListByUserId() {
        List<Long> userIds = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            userIds.add(26208708460552L + i);
        }
        List<FingerprintDO> results = fingerprintMapper.listByUserId(userIds);
        for (FingerprintDO result : results) {
            logger.info("{}", result);
        }
    }
}
