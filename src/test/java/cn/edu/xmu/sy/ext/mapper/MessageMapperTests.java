/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.com.lx1992.lib.util.UIDGenerateUtil;
import cn.edu.xmu.sy.ext.domain.MessageDO;
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
public class MessageMapperTests {
    private final Logger logger = LoggerFactory.getLogger(MessageMapperTests.class);

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSave() {
        MessageDO domain = new MessageDO();
        domain.setCounterId(26227809714182L);
        domain.setSessionId(26224043098119L);
        domain.setUid(UIDGenerateUtil.Standard.nextId());
        domain.setDirection(2);
        domain.setType("类型");
        domain.setBody("消息体");
        domain.setRetry(1);
        domain.setSendTime(LocalDateTime.now());
        domain.setAckTime(LocalDateTime.now().plusNanos(304759104));
        Assert.assertTrue(messageMapper.save(domain) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<MessageDO> domains = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MessageDO domain = new MessageDO();
            domain.setCounterId(78825709014876164L);
            domain.setSessionId(78830704477077515L);
            domain.setUid(UIDGenerateUtil.Standard.nextId());
            domain.setDirection(1);
            domain.setType("类型_" + i);
            domain.setBody("消息体_" + i);
            domain.setRetry(i);
            domain.setSendTime(LocalDateTime.now());
            domains.add(domain);
        }
        for (int i = 0; i < 5; i++) {
            MessageDO domain = new MessageDO();
            domain.setCounterId(78825709014876165L);
            domain.setSessionId(78830704477077517L);
            domain.setUid(UIDGenerateUtil.Standard.nextId());
            domain.setDirection(2);
            domain.setType("类型_" + i);
            domain.setBody("消息体_" + i);
            domain.setRetry(i);
            domain.setSendTime(LocalDateTime.now());
            domains.add(domain);
        }
        Assert.assertTrue(messageMapper.saveBatch(domains) == domains.size());
    }

    @Test
    public void testUpdateById() {
        MessageDO domain = new MessageDO();
        domain.setId(78835371441389574L);
        domain.setType("类型_修改");
        domain.setBody("消息体_修改");
        domain.setAckTime(LocalDateTime.now());
        Assert.assertTrue(messageMapper.updateById(domain) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(messageMapper.removeById(78835368757035009L) == 1);
    }

    @Test
    public void testRemoveByCounterId() {
        Assert.assertTrue(messageMapper.removeByCounterId(78825709014876164L) == 5);
    }


    @Test
    public void testRemoveBySessionId() {
        Assert.assertTrue(messageMapper.removeBySessionId(78825709014876164L, 78830704477077515L) == 5);
    }

    @Test
    public void testGetById() {
        MessageDO domain1 = messageMapper.getById(78835371441389574L);
        logger.info("testGetById --> {}", domain1);
        Assert.assertNotNull(domain1);

        MessageDO domain2 = messageMapper.getById(78835368757035009L);
        logger.info("testGetById --> {}", domain2);
        Assert.assertNull(domain2);
    }

    @Test
    public void testGetByUid() {
        List<MessageDO> domains1 = messageMapper.getByUid(9623458427910L, null);
        logger.info("testGetByUid --> {}", domains1);
        Assert.assertEquals(domains1.size(), 1);

        List<MessageDO> domains2 = messageMapper.getByUid(9623458427910L, 1);
        logger.info("testGetByUid --> {}", domains2);
        Assert.assertEquals(domains2.size(), 0);
    }

    @Test
    public void testListByUid() {
        List<Long> uids = new ArrayList<>();
        uids.add(9623458427912L);
        uids.add(9623458427913L);
        uids.add(9623458427914L);

        List<MessageDO> domains1 = messageMapper.listByUid(uids, 2);
        logger.info("testListByUid --> {}", domains1);
        Assert.assertEquals(domains1.size(), 3);

        List<MessageDO> domains2 = messageMapper.listByUid(uids, 1);
        logger.info("testListByUid --> {}", domains2);
        Assert.assertEquals(domains2.size(), 0);
    }
}
