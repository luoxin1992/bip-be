/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.UserCreateParam;
import cn.edu.xmu.sy.ext.param.UserDeleteParam;
import cn.edu.xmu.sy.ext.param.UserModifyParam;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luoxin
 * @version 2017-3-16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {
    private final Logger logger = LoggerFactory.getLogger(UserServiceTests.class);

    @Autowired
    private UserService userService;

    @Test
    public void testList() {
        UserQueryParam param = new UserQueryParam();
        //param.getSearch().setKeyword("测试用户9");
        param.getPaging().setStart(26208708460559L);
        param.getPaging().setSize(5);
        BasePagingResult<UserQueryResult> result = userService.query(param);
        logger.info("{}", result);
    }

    @Test
    public void testCreate() {
        //UserCreateParam param = new UserCreateParam();
        //userService.create(param);
    }

    @Test
    public void testUpdate() {
        //UserModifyParam param = new UserModifyParam();
        //param.setId(0L);
        //userService.modify(param);
    }

    @Test
    public void testDelete() {
        //UserDeleteParam param = new UserDeleteParam();
        //param.setId(0L);
        //userService.delete(param);
    }
}
