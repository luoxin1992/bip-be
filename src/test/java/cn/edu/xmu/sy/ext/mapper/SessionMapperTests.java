/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.SessionDO;
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
        SessionDO domain = new SessionDO();
        domain.setCounterId(0L);
        domain.setToken("Token");
        domain.setStatus(2);
        domain.setOnlineTime(LocalDateTime.now());
        domain.setOfflineTime(LocalDateTime.now().plusHours(3).plusMinutes(4).plusSeconds(5));
        Assert.assertTrue(sessionMapper.save(domain) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<SessionDO> domains = new ArrayList<>();
        for (long i = 78825709014876161L; i <= 78825709014876165L; i++) {
            for (int j = 1; j <= 2; j++) {
                SessionDO domain = new SessionDO();
                domain.setCounterId(i);
                domain.setToken("Token_" + j);
                domain.setStatus(1);
                domain.setOnlineTime(LocalDateTime.now());
                domains.add(domain);
            }
        }
        Assert.assertTrue(sessionMapper.saveBatch(domains) == domains.size());
    }

    @Test
    public void testUpdateById() {
        SessionDO sessionDO = new SessionDO();
        sessionDO.setId(78830704477077514L);
        sessionDO.setToken("Token_修改");
        sessionDO.setStatus(3);
        sessionDO.setOfflineTime(LocalDateTime.now());
        Assert.assertTrue(sessionMapper.updateById(sessionDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(sessionMapper.removeById(78830702170210307L) == 1);
    }

    @Test
    public void testRemoveByCounterId() {
        Assert.assertTrue(sessionMapper.removeByCounterId(78825709014876162L) == 2);
    }

    @Test
    public void testGetById() {
        SessionDO domain1 = sessionMapper.getById(78830704477077514L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        SessionDO domain2 = sessionMapper.getById(78830702170210307L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }

    @Test
    public void testGetByToken() {
        SessionDO domain = sessionMapper.getByToken("Token_5");
        logger.info("testGetByToken --> {}", domain);
        Assert.assertEquals(domain.getId().longValue(), 78830704477077513L);
    }

    @Test
    public void testListById() {
        List<Long> ids = new ArrayList<>();
        ids.add(78830704477077515L);
        ids.add(78830704477077516L);
        ids.add(78830704477077517L);

        List<SessionDO> domains = sessionMapper.listById(ids);
        logger.info("testListById --> {}", domains.size());
        Assert.assertEquals(domains.size(), 3);
    }

    @Test
    public void testGetByCounterId() {
        List<SessionDO> domains = sessionMapper.getByCounterId(78825709014876161L, 10);
        logger.info("testGetByCounterId --> {}", domains);
        Assert.assertEquals(domains.size(), 2);
    }

    @Test
    public void testListByCounterId() {
        List<Long> counterIds = new ArrayList<>();
        counterIds.add(78825709014876163L);
        counterIds.add(78825709014876164L);

        List<SessionDO> domains = sessionMapper.listByCounterId(counterIds);
        logger.info("testListByCounterId --> {}", domains);
        Assert.assertEquals(domains.size(), 2);
    }
}
