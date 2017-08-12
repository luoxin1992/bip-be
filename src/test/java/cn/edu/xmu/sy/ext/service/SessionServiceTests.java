/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

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
public class SessionServiceTests {
    private final Logger logger = LoggerFactory.getLogger(UserServiceTests.class);

    @Autowired
    private SessionService sessionService;

    @Test
    public void testOnline() {
        //SessionOnlineParam param = new SessionCreateParam();
        //param.setIp("192.168.1.173");
        //param.setMac("00200FE00005");
        //sessionService.online(param);
    }

    @Test
    public void testOffline() {
        //SessionOfflineParam param = new SessionOfflineParam();
        //param.setToken("83nde23k2");
        //sessionService.offline(param);
    }

    @Test
    public void testForceOffline() {
        //SessionForceOfflineParam param = new SessionForceOfflineParam();
        //param.setId(34523110703106L);
        //sessionService.forceOffline(param);
    }

    @Test
    public void testQuery() {
        //SessionQueryParam param = new SessionQueryParam();
        //param.setCounterId(26224043098124L);
        //param.setStatus(2);
        //logger.info("{}", sessionService.query(param));
    }

    @Test
    public void testQueryBatch() {
        //BasePeriodParam period = new BasePeriodParam();
        //period.setStart(DateTimeUtil.getTodayAtStart());
        //period.setEnd(DateTimeUtil.getTodayAtEnd());
        //List<Long> counterIds = new ArrayList<>();
        //counterIds.add(34508304809987L);
        //SessionBatchQueryParam param = new SessionQueryBatchParam();
        //param.setCounterIds(counterIds);
        //param.setPeriod(period);
        //logger.info("{}", sessionService.queryBatch(param));
    }

    @Test
    public void testGetOnline() {
        //logger.info("{}", sessionService.getOnlineIdByCounterIdOptional(26224043098124L));
    }
}
