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
 * @version 2017-3-28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServiceTests {
    private final Logger logger = LoggerFactory.getLogger(UserServiceTests.class);

    @Autowired
    private ResourceService resourceService;

    @Test
    public void testCreate() {
        //String[] name = {"有许多应用程序和Web站点只有在安装Java后才能正常工作", "如果您在构建嵌入式或消费类设备并且想包括Java",
        //        "JRE允许用Java编程语言编写的小应用程序在各种浏览器中运行", "Java是由Sun Microsystems在1995年首先发布的编程语言和计算平台",
        //        "Java虚拟机只是Java软件中参与Web交互的一个方面", "Java虚拟机内置于您下载的Java软件中", "而且这样的应用程序和Web站点日益增多",
        //        "安装此免费更新将确保您的Java应用程序能够继续安全有效地运行", "在Web浏览器上只需JRE便可运行Java软件", "提高了在您的机器上运行的Java应用程序的性能",
        //        "请联系Oracle以获取有关在您的设备中包括Java的详细信息"};
        //String[] path = {"06725f66aa514b27a749ae731f9e693a", "06faa2bf52cf4398b58359b5777a3f39",
        //        "0b5d2622454b470b9196029faf26afea", "4cad71c4db6c48acac97016dc93aa7fd",
        //        "57a6572025884f858c308ea07243a5ac", "6524ec3a850c4deeb83d6b845ee1eaed",
        //        "77d669e71b044355924c9b4a7c5ad8e7", "b094358aca9f4cefa68c79cf214070ea",
        //        "c7bb81b197754e279d2c5ab63bb5bfc3", "e8e5813a3cdf4258a8c90ef5d57f0d9e",
        //        "ef576a1718534d799339a7fdb9fda28f"};
        //for (int i = 0; i < 11; i++) {
        //    resourceService.create(ResourceTypeEnum.VOICE.getType(), name[i], "/tmp/voice/" + path[i]);
        //}
    }

    @Test
    public void testGetIdByTypeAndName() {
        //logger.info("{}", resourceService.getIdByTypeAndName("voice", "安装此免费更新将确保您的Java应用程序能够继续安全有效地运行"));
    }

    @Test
    public void testList() {
        //logger.info("{}", resourceService.list());
    }

    @Test
    public void testQuery() {
        //BasePeriodParam period = new BasePeriodParam();
        //period.setStart("2017-03-28 08:01:53");
        //period.setEnd("2017-03-28 08:01:53");
        //BasePagingParam paging = new BasePagingParam();
        //paging.setSize(5);
        //paging.setStart(31362123825159L);
        //ResourceQueryParam param = new ResourceQueryParam();
        //param.setType("voice");
        //param.setPeriod(period);
        //param.setPaging(paging);
        //logger.info("{}", resourceService.query(param));
    }
}
