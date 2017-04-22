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
 * @version 2017-4-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TtsServiceTests {
    private final Logger logger = LoggerFactory.getLogger(TtsServiceTests.class);
    @Autowired
    private TtsService ttsService;

    @Test
    public void testTtsAsync() {
        //ttsService.ttsAsync(null, "测试语音合成", false);
    }

    @Test
    public void testTtsSync() {
        //String result = ttsService.ttsSync(null,
        //        "厦门大学信息科学与技术学院，简称信息学院，坐落在厦门大学海韵园内，毗邻美丽的珍珠湾。", false);
        //logger.info("{}", result);
    }

    @Test
    public void testTtsBatchAsync() {
        //List<String> list = new ArrayList<>();
        //list.add("送杜少府之任蜀州");
        //list.add("城阙辅三秦，风烟望五津。");
        //list.add("与君离别意，同是宦游人。");
        //list.add("海内存知己，天涯若比邻。");
        //list.add("无为在岐路，儿女共沾巾。");
        //ttsService.ttsBatchAsync(null, list, false);
    }

    @Test
    public void testTtsBatchSync() {
        //List<String> list = new ArrayList<>();
        //list.add("送杜少府之任蜀州");
        //list.add("城阙辅三秦，风烟望五津。");
        //list.add("与君离别意，同是宦游人。");
        //list.add("海内存知己，天涯若比邻。");
        //list.add("无为在岐路，儿女共沾巾。");
        //List<String> result = ttsService.ttsBatchSync(null, list, false);
        //logger.info("{}", result);
    }
}
