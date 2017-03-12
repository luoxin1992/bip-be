/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.com.lx1992.lib.util.IDGenerator;
import cn.edu.xmu.sy.ext.domain.UserDO;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
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
        userDO.setId(IDGenerator.nextId(0, 0));
        userDO.setNumber("10001");
        userDO.setName("测试");
        userDO.setPhoto("/upload/photo/1.jpg");
        userDO.setGmtCreate(LocalDateTime.now());
        userDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(userMapper.save(userDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<UserDO> userDOs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserDO userDO = new UserDO();
            userDO.setId(IDGenerator.nextId(0, 0));
            userDO.setNumber("1000" + i);
            userDO.setName("测试" + i);
            userDO.setPhoto("/upload/photo/" + i + ".jpg");
            userDO.setGmtCreate(LocalDateTime.now());
            userDO.setGmtModify(LocalDateTime.now());
            userDOs.add(userDO);
        }
        Assert.assertTrue(userMapper.saveBatch(userDOs) == userDOs.size());
    }

    @Test
    public void testUpdateById() {
        UserDO userDO = new UserDO();
        userDO.setId(25495383048197L);
        userDO.setNumber("10001_1");
        userDO.setName("测试_修改");
        userDO.setPhoto("/upload/photo/1_1.jpg");
        userDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(userMapper.updateById(userDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(userMapper.removeById(25495383048197L) == 1);
    }

    @Test
    public void testGetById() {
        logger.info("{}", ToStringBuilder.reflectionToString(userMapper.getById(25494732931075L)));
    }

    @Test
    public void testListByKeyword() {
        UserQueryParam param = new UserQueryParam();
        param.setKeyword("测试9");
        //param.setMinId(25494732931080L);
        param.setPageSize(5);
        List<UserQueryResult> results = userMapper.listByKeyword(param);
        for (UserQueryResult result : results) {
            logger.info("{}", ToStringBuilder.reflectionToString(result));
        }
    }

    @Test
    public void testCountByKeyword() {
        UserQueryParam param = new UserQueryParam();
        param.setKeyword("测试9");
        Assert.assertTrue(userMapper.countByKeyword(param) == 1);
    }
}
