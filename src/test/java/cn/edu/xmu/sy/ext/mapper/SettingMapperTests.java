/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.SettingDO;
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
public class SettingMapperTests {
    private final Logger logger = LoggerFactory.getLogger(SettingMapperTests.class);

    @Autowired
    private SettingMapper settingMapper;

    @Test
    public void testSave() {
        SettingDO domain = new SettingDO();
        domain.setParentId(0L);
        domain.setKey("键");
        domain.setDescription("描述");
        Assert.assertTrue(settingMapper.save(domain) == 1);
        Assert.assertTrue(settingMapper.save(domain) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<SettingDO> domains = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SettingDO domain = new SettingDO();
            domain.setParentId(78811545047400454L);
            domain.setKey("键_" + i);
            domain.setValue("值_" + i);
            domain.setRegExp("\\d{" + i + "}");
            domain.setDescription("描述_" + i);
            domains.add(domain);
        }
        for (int i = 5; i < 10; i++) {
            SettingDO domain = new SettingDO();
            domain.setParentId(78811547966636041L);
            domain.setKey("键_" + i);
            domain.setValue("值_" + i);
            domain.setRegExp("\\d{" + i + "}");
            domain.setDescription("描述_" + i);
            domains.add(domain);
        }
        Assert.assertTrue(settingMapper.saveBatch(domains) == domains.size());
    }

    @Test
    public void testUpdateById() {
        SettingDO domain = new SettingDO();
        domain.setId(78812174880866318L);
        domain.setKey("键_修改");
        domain.setValue("值_修改");
        domain.setRegExp("\\w+");
        domain.setDescription("内容_修改");
        Assert.assertTrue(settingMapper.updateById(domain) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(settingMapper.removeById(78812174880866315L) == 1);
    }

    @Test
    public void testRemoveByParentId() {
        Assert.assertTrue(settingMapper.removeByParentId(78811545047400454L) == 6);
    }

    @Test
    public void testGetById() {
        SettingDO domain1 = settingMapper.getById(78812174880866316L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        SettingDO domain2 = settingMapper.getById(78812174880866315L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }

    @Test
    public void testGetByKey() {
        SettingDO domain = settingMapper.getByKey("键_8");
        logger.info("testGetByKey --> {}", domain);
        Assert.assertNotNull(domain);
    }
}
