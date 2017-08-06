/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.com.lx1992.lib.util.UIDGenerateUtil;
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
        FingerprintDO domain = new FingerprintDO();
        domain.setUserId(0L);
        domain.setUid(UIDGenerateUtil.Compact.nextId());
        domain.setFinger("手指");
        domain.setTemplate("模板");
        domain.setEnrollTime(LocalDateTime.of(2017, 1, 13, 15, 23, 39));
        domain.setIdentifyTime(LocalDateTime.of(2017, 2, 28, 8, 17, 56));
        Assert.assertTrue(fingerprintMapper.save(domain) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<FingerprintDO> domains = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (long j = 78817317265342476L; j <= 78817317265342478L; j++) {
                FingerprintDO domain = new FingerprintDO();
                domain.setUserId(j);
                domain.setUid(UIDGenerateUtil.Compact.nextId());
                domain.setFinger("手指_" + i);
                domain.setTemplate("模板_" + i);
                domain.setEnrollTime(LocalDateTime.now());
                domains.add(domain);
            }
        }
        Assert.assertTrue(fingerprintMapper.saveBatch(domains) == domains.size());
    }

    @Test
    public void testUpdateById() {
        FingerprintDO domain = new FingerprintDO();
        domain.setId(78820791101161475L);
        domain.setFinger("手指_修改");
        domain.setTemplate("模板_修改");
        Assert.assertTrue(fingerprintMapper.updateById(domain) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(fingerprintMapper.removeById(78820785627594761L) == 1);
    }

    @Test
    public void testRemoveByUserId() {
        Assert.assertTrue(fingerprintMapper.removeByUserId(78817317265342477L) == 3);
    }

    @Test
    public void testGetById() {
        FingerprintDO domain1 = fingerprintMapper.getById(78820791101161475L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        FingerprintDO domain2 = fingerprintMapper.getById(78820785627594761L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }

    @Test
    public void testGetByUid() {
        FingerprintDO domain = fingerprintMapper.getByUid(150338725);
        logger.info("testGetByUid --> {}", domain);
        Assert.assertEquals(domain.getId().longValue(), 78820791101161477L);
    }

    @Test
    public void testCountByUserId() {
        Assert.assertEquals(fingerprintMapper.countByUserId(78817317265342476L).longValue(), 3);
    }

    @Test
    public void testGetByUserId() {
        List<FingerprintDO> domains = fingerprintMapper.getByUserId(78817317265342476L);
        logger.info("testGetByUserId --> {}", domains.size());
        Assert.assertEquals(domains.size(), 3);
    }

    @Test
    public void testListByUserId() {
        List<Long> userIds = new ArrayList<>();
        userIds.add(78817317265342476L);
        userIds.add(78817317265342478L);

        List<FingerprintDO> domains = fingerprintMapper.listByUserId(userIds);
        logger.info("testListByUserId --> {}", domains.size());
        Assert.assertEquals(domains.size(), 6);
    }

    @Test
    public void testCountAll() {
        Assert.assertEquals(fingerprintMapper.countAll().longValue(), 6);
    }

    @Test
    public void testListAll() {
        List<FingerprintDO> domains = fingerprintMapper.listAll(0L, 100);
        logger.info("testListAll --> {}", domains.size());
        Assert.assertEquals(domains.size(), 6);
    }
}
