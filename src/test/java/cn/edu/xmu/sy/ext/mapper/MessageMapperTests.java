/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

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
        MessageDO messageDO = new MessageDO();
        messageDO.setCounterId(26227809714182L);
        messageDO.setSessionId(26224043098119L);
        messageDO.setType("测试");
        messageDO.setExtra("测试");
        messageDO.setSendTime(LocalDateTime.now());
        messageDO.setGmtCreate(LocalDateTime.now());
        messageDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(messageMapper.save(messageDO) == 1);
    }

    @Test
    public void testSaveBatch() {
        List<MessageDO> messageDOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 1; k <= 2; k++) {
                    MessageDO messageDO = new MessageDO();
                    messageDO.setCounterId(26224043098119L + i);
                    messageDO.setSessionId(26227809714182L + i * 2 + j);
                    messageDO.setType("测试类别" + k);
                    messageDO.setExtra("测试内容" + k);
                    messageDO.setSendTime(LocalDateTime.now());
                    messageDO.setGmtCreate(LocalDateTime.now());
                    messageDO.setGmtModify(LocalDateTime.now());
                    messageDOs.add(messageDO);
                }
            }
        }
        Assert.assertTrue(messageMapper.saveBatch(messageDOs) == messageDOs.size());
    }

    @Test
    public void testUpdateById() {
        MessageDO messageDO = new MessageDO();
        messageDO.setId(26286379106309L);
        messageDO.setCounterId(26227071516679L);
        messageDO.setSessionId(26223598501888L);
        messageDO.setType("测试_修改");
        messageDO.setExtra("测试_修改");
        messageDO.setAckTime(LocalDateTime.now());
        messageDO.setGmtModify(LocalDateTime.now());
        Assert.assertTrue(messageMapper.updateById(messageDO) == 1);
    }

    @Test
    public void testRemoveById() {
        Assert.assertTrue(messageMapper.removeById(26286379106309L) == 1);
    }

    @Test
    public void testRemoveByCounterId() {
        Assert.assertTrue(messageMapper.removeByCounterId(26227071516679L) == 1);
    }


    @Test
    public void testRemoveBySessionId() {
        Assert.assertTrue(messageMapper.removeBySessionId(26224043098119L) == 2);
    }

    @Test
    public void testGetById() {
        logger.info("{}", messageMapper.getById(26286379106309L));
    }

    @Test
    public void testGetByCounterId() {
        List<MessageDO> results = messageMapper.getByCounterId(26227071516679L);
        for (MessageDO result : results) {
            logger.info("{}", result);
        }
    }

    @Test
    public void testGetBySessionId() {
        List<MessageDO> results = messageMapper.getBySessionId(26224043098119L);
        for (MessageDO result : results) {
            logger.info("{}", result);
        }
    }

    @Test
    public void testListByCounterId() {
        List<Long> counterIds = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            counterIds.add(26224043098119L + i);
        }
        List<MessageDO> results = messageMapper.listByCounterId(counterIds);
        for (MessageDO result : results) {
            logger.info("{}", result);
        }
    }

    @Test
    public void testListBySessionId() {
        List<Long> sessionIds = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            sessionIds.add(26227809714182L + i);
        }
        List<MessageDO> results = messageMapper.listBySessionId(sessionIds);
        for (MessageDO result : results) {
            logger.info("{}", result);
        }
    }
}
