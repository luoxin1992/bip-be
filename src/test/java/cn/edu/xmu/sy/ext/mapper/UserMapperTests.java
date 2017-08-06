/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.UserDO;
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
 * @version 2017-3-10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTests {
    private final Logger logger = LoggerFactory.getLogger(UserMapperTests.class);

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSave() {
        UserDO userDO = new UserDO();
        userDO.setNumber("编号");
        userDO.setName("姓名");
        userDO.setPhoto("http://www.google.com");
        Assert.assertTrue(userMapper.save(userDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<UserDO> userDOs = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            UserDO userDO = new UserDO();
            userDO.setNumber("编号_" + i);
            userDO.setName("姓名_" + i);
            userDO.setPhoto("http://www.domain.com/upload/photo/" + i + ".jpg");
            userDOs.add(userDO);
        }
        Assert.assertTrue(userMapper.saveBatch(userDOs) == userDOs.size());
    }

    @Test
    public void testUpdateById() {
        UserDO userDO = new UserDO();
        userDO.setId(78817317265342475L);
        userDO.setNumber("编号_修改");
        userDO.setName("姓名_修改");
        userDO.setPhoto("http://www.domain.com/upload/photo/6_修改.jpg");
        Assert.assertTrue(userMapper.updateById(userDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(userMapper.removeById(78817317265342471L) == 1);
    }

    @Test
    public void testGetById() {
        UserDO domain1 = userMapper.getById(78817317265342475L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        UserDO domain2 = userMapper.getById(78817317265342471L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }

    @Test
    public void testGetByNumber() {
        UserDO domain = userMapper.getByNumber("编号_5");
        logger.info("testGetByNumber --> {}", domain);
        Assert.assertNotNull(domain);
    }
}
