/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.UserSyncParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luoxin
 * @version 2017-3-23
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSyncServiceTests {
    private final Logger logger = LoggerFactory.getLogger(UserSyncServiceTests.class);

    @Autowired
    private UserSyncService userSyncService;

    @Test
    public void testCreate() {

    }

    @Test
    public void testModify() {

    }

    @Test
    public void testDelete() {
        UserSyncParam param = new UserSyncParam();
        param.setNumber("10002");
        userSyncService.delete(param);
    }
}
