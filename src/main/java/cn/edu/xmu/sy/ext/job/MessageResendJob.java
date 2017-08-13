/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.job;

import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.constant.DateTimeConstant;
import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.edu.xmu.sy.ext.constant.RedisKeyConstant;
import cn.edu.xmu.sy.ext.disconf.MessageConfigItem;
import cn.edu.xmu.sy.ext.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Message重新发送Job
 *
 * @author luoxin
 * @version 2017-4-28
 */
@CatTransaction
@Component
public class MessageResendJob {
    private final Logger logger = LoggerFactory.getLogger(MessageResendJob.class);

    @Autowired
    private MessageService messageService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private MessageConfigItem messageConfig;

    @PreDestroy
    public void destroy() {
        String pattern = RedisKeyConstant.RETRY_PREFIX + CommonConstant.ASTERISK_STRING;
        redisTemplate.keys(pattern).forEach(key -> redisTemplate.delete(key));
    }

    @Scheduled(cron = "0/1 * * * * ?")
    public void execute() {
        int slot = DateTimeUtil.getNow().getSecond();
        retry(slot);
    }

    /**
     * 计划下次重试
     *
     * @param id 消息ID
     */
    public void schedule(Long id) {
        int second = DateTimeUtil.getNow().getSecond();
        int slot = (second + messageConfig.getRetryPeriod()) % DateTimeConstant.SECOND_PRE_MINUTE;
        String key = RedisKeyConstant.RETRY_PREFIX + slot;

        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        setOps.add(key, String.valueOf(id));

        setSlotIndex(id, slot);
        logger.info("schedule resend for message {} at slot {}", id, slot);
    }

    /**
     * 取消已计划的重试
     *
     * @param id 消息ID
     */
    public void cancel(Long id) {
        Integer slot = getSlotIndex(id);
        if (slot == null) {
            logger.warn("no scheduled resend for message {}", id);
            return;
        }
        String key = RedisKeyConstant.RETRY_PREFIX + slot;

        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        setOps.remove(key, id);

        setSlotIndex(id, null);
        logger.info("cancel scheduled resend for message {} at slot {}", id, slot);
    }

    /**
     * 执行重试任务
     *
     * @param slot slot
     */
    private void retry(int slot) {
        String key = RedisKeyConstant.RETRY_PREFIX + slot;
        String id;

        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        while ((id = setOps.pop(key)) != null) {
            setSlotIndex(Long.valueOf(id), null);
            messageService.resend(Long.valueOf(id));
            logger.info("resend message {}", id);
        }
    }

    /**
     * 设置消息ID到slot的索引
     * 若slot为null，则删除索引
     *
     * @param id   消息ID
     * @param slot slot
     */
    private void setSlotIndex(Long id, Integer slot) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        if (slot == null) {
            hashOps.delete(RedisKeyConstant.RETRY_INDEX, String.valueOf(id));
        } else {
            hashOps.put(RedisKeyConstant.RETRY_INDEX, String.valueOf(id), String.valueOf(slot));
        }
    }

    /**
     * 查询消息ID到slot的索引
     *
     * @param id 消息ID
     * @return slot
     */
    private Integer getSlotIndex(Long id) {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        String slot = hashOps.get(RedisKeyConstant.RETRY_INDEX, String.valueOf(id));
        return slot == null ? null : Integer.valueOf(slot);
    }
}
