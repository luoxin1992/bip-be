/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author luoxin
 * @version 2017-4-8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeechServiceTest {
    @Autowired
    private SpeechService speechService;

    @Test
    public void testSynthesizeSync() {
        //List<String> list = new ArrayList<>();
        //list.add("海内存知己，天涯若比邻");
        //speechService.synthesizeAsync(list);
    }
}
