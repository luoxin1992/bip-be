/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.component;

import cn.edu.xmu.sy.ext.result.FingerprintListTemplateResult;
import cn.edu.xmu.sy.ext.service.FingerprintSdkService;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 指纹仪SDK内部缓冲数据装载器
 *
 * @author LuoXin
 * @version 2017/5/6
 */
@Component
public class FingerprintSdkCacheLoader implements CommandLineRunner {
    @Autowired
    private FingerprintService fingerprintService;
    @Autowired
    private FingerprintSdkService fingerprintSdkService;

    @Override
    public void run(String... strings) {
        List<FingerprintListTemplateResult> results = fingerprintService.listTemplate().getList();
        //直接写入SDK 不检查既有模板是否重复
        results.forEach(result -> fingerprintSdkService.enroll(result.getUid(), result.getTemplate()));
    }
}
