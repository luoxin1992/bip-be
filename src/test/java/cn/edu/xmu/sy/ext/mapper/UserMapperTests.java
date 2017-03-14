/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.com.lx1992.lib.util.IDGenerator;
import cn.edu.xmu.sy.ext.domain.UserDO;
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
        userDO.setId(IDGenerator.nextId(EntityEnum.USER.getCode(), WorkerEnum.DEFAULT.getCode()));
        userDO.setNumber("00000000");
        userDO.setName("测试");
        userDO.setGmtCreate(LocalDateTime.now());
        userDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(userMapper.save(userDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<UserDO> userDOs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserDO userDO = new UserDO();
            userDO.setId(IDGenerator.nextId(EntityEnum.USER.getCode(), WorkerEnum.DEFAULT.getCode()));
            userDO.setNumber("0000100" + i);
            userDO.setName("测试用户" + i);
            userDO.setPhoto("/upload/photo/0000100" + i + ".jpg");
            userDO.setGmtCreate(LocalDateTime.now());
            userDO.setGmtModify(LocalDateTime.now());
            userDOs.add(userDO);
        }
        Assert.assertTrue(userMapper.saveBatch(userDOs) == userDOs.size());
    }

    @Test
    public void testUpdateById() {
        UserDO userDO = new UserDO();
        userDO.setId(26208150618113L);
        userDO.setNumber("00000000_修改");
        userDO.setName("测试_修改");
        userDO.setPhoto("/upload/photo/00000000_修改.jpg");
        userDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(userMapper.updateById(userDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(userMapper.removeById(26208150618113L) == 1);
    }

    @Test
    public void testGetById() {
        logger.info("{}", userMapper.getById(26208150618113L));
    }

    //@Test
    //public void testListByKeyword() {
    //    UserQueryParam param = new UserQueryParam();
    //    //param.setKeyword("测试9");
    //    //param.setMinId(25494732931080L);
    //    //param.setPageSize(5);
    //    List<UserQueryResult> results = userMapper.listByKeyword(param);
    //    for (UserQueryResult result : results) {
    //        logger.info("{}", ToStringBuilder.reflectionToString(result));
    //    }
    //}
    //
    //@Test
    //public void testCountByKeyword() {
    //    UserQueryParam param = new UserQueryParam();
    //    //param.setKeyword("测试9");
    //    Assert.assertTrue(userMapper.countByKeyword(param) == 1);
    //}
}
