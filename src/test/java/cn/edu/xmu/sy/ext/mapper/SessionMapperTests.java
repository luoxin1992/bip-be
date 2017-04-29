/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.SessionDO;
import cn.edu.xmu.sy.ext.param.SessionBatchQueryParam;
import cn.edu.xmu.sy.ext.param.SessionQueryParam;
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
 * @version 2017-3-14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionMapperTests {
    private final Logger logger = LoggerFactory.getLogger(SessionMapperTests.class);

    @Autowired
    private SessionMapper sessionMapper;

    @Test
    public void testSave() {
        SessionDO sessionDO = new SessionDO();
        sessionDO.setCounterId(26223598501888L);
        sessionDO.setToken("");
        sessionDO.setStatus(1);
        sessionDO.setOnlineTime(LocalDateTime.now());
        sessionDO.setGmtCreate(LocalDateTime.now());
        sessionDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(sessionMapper.save(sessionDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<SessionDO> sessionDOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j <= 2; j++) {
                SessionDO sessionDO = new SessionDO();
                sessionDO.setCounterId(26224043098119L + i);
                sessionDO.setToken("");
                sessionDO.setStatus(1);
                sessionDO.setOnlineTime(LocalDateTime.now());
                sessionDO.setGmtCreate(LocalDateTime.now());
                sessionDO.setGmtModify(LocalDateTime.now());
                sessionDOs.add(sessionDO);
            }
        }
        Assert.assertTrue(sessionMapper.saveBatch(sessionDOs) == sessionDOs.size());
    }

    @Test
    public void testUpdateById() {
        SessionDO sessionDO = new SessionDO();
        sessionDO.setId(26226647891969L);
        sessionDO.setCounterId(0L);
        sessionDO.setToken("1234567890ABCDEF");
        sessionDO.setStatus(2);
        sessionDO.setOfflineTime(LocalDateTime.now());
        sessionDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(sessionMapper.updateById(sessionDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(sessionMapper.removeById(26226647891969L) == 1);
    }

    @Test
    public void testRemoveByCounterId() {
        Assert.assertTrue(sessionMapper.removeByCounterId(26223598501888L) == 2);
    }

    @Test
    public void testGetById() {
        logger.info("{}", ToStringBuilder.reflectionToString(sessionMapper.getById(34523110703106L)));
    }

    @Test
    public void testGetByCounterId() {
        List<SessionDO> results = sessionMapper.getByCounterId(26224043098119L);
        for (SessionDO result : results) {
            logger.info("{}", result);
        }
    }

    @Test
    public void testListByCounterId() {
        List<Long> counterIds = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            counterIds.add(26224043098120L + i);
        }
        List<SessionDO> results = sessionMapper.listByCounterId(counterIds);
        for (SessionDO result : results) {
            logger.info("{}", result);
        }
    }

    @Test
    public void testGetByParam() {
        SessionQueryParam param = new SessionQueryParam();
        param.setCounterId(26224043098121L);
        param.setStatus(1);
        //param.setPeriod(new BasePeriodParam(DateTimeUtil.getTodayStr(DateTimeConstant.DATE_PATTERN)));
        logger.info("{}", sessionMapper.getByParam(param));
    }

    @Test
    public void testListByParam() {
        List<Long> counterIds = new ArrayList<>();
        counterIds.add(26224043098122L);
        counterIds.add(26224043098123L);
        SessionBatchQueryParam param = new SessionBatchQueryParam();
        param.setCounterIds(counterIds);
        param.setStatus(2);
        //param.setPeriod(new BasePeriodParam(DateTimeUtil.getTodayStr(DateTimeConstant.DATE_PATTERN)));
        logger.info("{}", sessionMapper.listByParam(param));
    }

    @Test
    public void testGetOnlineSessionId() {
        logger.info("{}", sessionMapper.getOnlineIdByCounterId(26224043098121L));
    }
}
