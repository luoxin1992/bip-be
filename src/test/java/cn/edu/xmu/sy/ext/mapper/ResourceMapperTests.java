/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.ResourceDO;
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
public class ResourceMapperTests {
    private final Logger logger = LoggerFactory.getLogger(ResourceMapperTests.class);

    @Autowired
    private ResourceMapper resourceMapper;

    @Test
    public void testSave() {
        ResourceDO domain = new ResourceDO();
        domain.setType("类型");
        domain.setTag("标签");
        domain.setFilename("文件名");
        domain.setMd5("MD5");
        Assert.assertTrue(resourceMapper.save(domain) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<ResourceDO> domains = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                ResourceDO domain = new ResourceDO();
                domain.setType("类型_" + i);
                domain.setTag("标签_" + j);
                domain.setFilename("文件名_" + j);
                domain.setMd5("MD5_" + j);
                domains.add(domain);
            }
        }
        Assert.assertTrue(resourceMapper.saveBatch(domains) == domains.size());
    }

    @Test
    public void testUpdateById() {
        ResourceDO domain = new ResourceDO();
        domain.setId(78808120993775617L);
        domain.setType("类型_修改");
        domain.setTag("标签_修改");
        domain.setFilename("文件名_修改");
        domain.setMd5("MD5_修改");
        Assert.assertTrue(resourceMapper.updateById(domain) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(resourceMapper.removeById(78808120993775622L) == 1);
    }

    @Test
    public void testGetById() {
        ResourceDO domain1 = resourceMapper.getById(78808120993775617L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        ResourceDO domain2 = resourceMapper.getById(78808120993775622L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }

    @Test
    public void testGetByType() {
        List<ResourceDO> domains = resourceMapper.getByType("类型_0");
        logger.info("testGetByType --> {}", domains);
        Assert.assertTrue(domains.size() == 2);
    }

    @Test
    public void testGetByTag() {
        ResourceDO domain = resourceMapper.getByTag("类型_2", "标签_1");
        logger.info("testGetByTag --> {}", domain);
        Assert.assertNotNull(domain);
    }

    @Test
    public void testCountAll() {
        Assert.assertTrue(resourceMapper.countAll() == 10);
    }

    @Test
    public void testListAll() {
        List<ResourceDO> domains = resourceMapper.listAll(0L, 100);
        logger.info("testListAll --> {}", domains.size());
        Assert.assertTrue(domains.size() == 10);
    }
}
