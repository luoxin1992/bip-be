/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.com.lx1992.lib.util.IDGenerator;
import cn.edu.xmu.sy.ext.domain.ResourceDO;
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
public class ResourceMapperTests {
    private final Logger logger = LoggerFactory.getLogger(ResourceMapperTests.class);

    @Autowired
    private ResourceMapper resourceMapper;

    @Test
    public void testSave() {
        ResourceDO resourceDO = new ResourceDO();
        resourceDO.setId(IDGenerator.nextId(EntityEnum.RESOURCE.getCode(), WorkerEnum.DEFAULT.getCode()));
        resourceDO.setType("测试");
        resourceDO.setName("测试");
        resourceDO.setPath("测试");
        resourceDO.setMd5("测试");
        resourceDO.setGmtCreate(LocalDateTime.now());
        resourceDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(resourceMapper.save(resourceDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<ResourceDO> resourceDOs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ResourceDO resourceDO = new ResourceDO();
            resourceDO.setId(IDGenerator.nextId(EntityEnum.RESOURCE.getCode(), WorkerEnum.DEFAULT.getCode()));
            resourceDO.setType("测试类型" + i);
            resourceDO.setName("测试名称" + i);
            resourceDO.setPath("测试路径" + i);
            resourceDO.setMd5("测试MD5" + i);
            resourceDO.setGmtCreate(LocalDateTime.now());
            resourceDO.setGmtModify(LocalDateTime.now());
            resourceDOs.add(resourceDO);
        }
        Assert.assertTrue(resourceMapper.saveBatch(resourceDOs) == resourceDOs.size());
    }

    @Test
    public void testUpdateById() {
        ResourceDO resourceDO = new ResourceDO();
        resourceDO.setId(26281711108103L);
        resourceDO.setType("测试_修改");
        resourceDO.setName("测试_修改");
        resourceDO.setPath("测试_修改");
        resourceDO.setMd5("测试_修改");
        resourceDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(resourceMapper.updateById(resourceDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(resourceMapper.removeById(26281711108103L) == 1);
    }

    @Test
    public void testGetById() {
        logger.info("{}", resourceMapper.getById(26281711108103L));
    }
}
