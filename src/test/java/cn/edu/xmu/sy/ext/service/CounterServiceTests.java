/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.param.BasePagingParam;
import cn.com.lx1992.lib.base.param.BaseSearchParam;
import cn.edu.xmu.sy.ext.param.CounterCreateParam;
import cn.edu.xmu.sy.ext.param.CounterModifyParam;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luoxin
 * @version 2017-4-6
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CounterServiceTests {
    private final Logger logger = LoggerFactory.getLogger(UserServiceTests.class);

    @Autowired
    private CounterService counterService;

    @Test
    public void testCreate() {
        CounterCreateParam param = new CounterCreateParam();
        param.setNumber("99");
        param.setName("测试窗口99");
        param.setMac("000000000099");
        param.setIp("192.168.1.199");
        counterService.create(param);
    }

    @Test
    public void testModify() {
        CounterModifyParam param = new CounterModifyParam();
        param.setId(34508304809987L);
        param.setNumber("ZXP54GX4");
        param.setName("个人业务");
        param.setMac("00200FE00005");
        param.setIp("192.168.1.172");
        counterService.modify(param);
    }

    @Test
    public void testDelete() {
        //counterService.delete(26224043098124L);
    }

    @Test
    public void testQuery() {
        BaseSearchParam search = new BaseSearchParam();
        BasePagingParam paging = new BasePagingParam();
        paging.setSize(5);
        CounterQueryParam param = new CounterQueryParam();
        param.setSearch(search);
        param.setPaging(paging);
        logger.info("{}", counterService.query(param));
    }
}
