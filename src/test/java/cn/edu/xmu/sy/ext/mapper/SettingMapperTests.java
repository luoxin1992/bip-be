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

import java.time.LocalDateTime;
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
        SettingDO settingDO = new SettingDO();
        settingDO.setParent(32762111197193L);
        settingDO.setKey("misc-user-mgr-enable");
        settingDO.setValue("true");
        settingDO.setDescription("启用用户管理功能");
        Assert.assertTrue(settingMapper.save(settingDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<SettingDO> settingDOs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SettingDO settingDO = new SettingDO();
            //settingDO.setCategory("测试类别" + i);
            //settingDO.setPropName("测试属性名" + i);
            //settingDO.setPropValue("测试属性值" + i);
            settingDO.setGmtCreate(LocalDateTime.now());
            settingDO.setGmtModify(LocalDateTime.now());
            settingDOs.add(settingDO);
        }
        Assert.assertTrue(settingMapper.saveBatch(settingDOs) == settingDOs.size());
    }

    @Test
    public void testUpdateById() {
        SettingDO settingDO = new SettingDO();
        settingDO.setId(26296130994177L);
        //settingDO.setCategory("测试_修改");
        //settingDO.setPropName("测试_修改");
        //settingDO.setPropValue("测试_修改");
        settingDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(settingMapper.updateById(settingDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(settingMapper.removeById(26296130994177L) == 1);
    }

    @Test
    public void testGetById() {
        logger.info("{}", settingMapper.getById(26296130994177L));
    }
}
