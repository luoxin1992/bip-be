/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.NativeConstant;
import cn.com.lx1992.lib.util.NativeUtil;
import cn.edu.xmu.sy.ext.constant.FingerprintConstant;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.service.FingerprintSdkService;
import fpscanner.ClassFactory;
import fpscanner.IFPScannerObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author LuoXin
 * @version 2017/5/6
 */
@CatTransaction
@Service
public class FPScannerSdkServiceImpl implements FingerprintSdkService {
    private final Logger logger = LoggerFactory.getLogger(FPScannerSdkServiceImpl.class);

    private IFPScannerObject sdk;
    private ExecutorService executor;

    @PostConstruct
    private void initialize() {
        checkNativeEnv();

        //SDK无线程安全保证，只能单线程运行
        sdk = ClassFactory.createIFPScannerObject();
        executor = Executors.newSingleThreadExecutor();

        createSdkCache();
        logger.info("initialize fingerprint sdk");
    }

    @PreDestroy
    private void destroy() throws InterruptedException {
        executor.shutdown();
        executor.awaitTermination(180, TimeUnit.SECONDS);
        //TODO 内存泄露 未提供释放内部缓冲函数
        sdk.dispose();
        logger.info("destroy fingerprint sdk");
    }

    @Override
    public int check(String template) {
        Supplier<Integer> supplier = () -> {
            int result = sdk.verifyRegTemplateFromStr(template);
            logger.info("check in-sdk template duplicate get result {}", result);
            return result;
        };
        return invokeMethod(supplier);
    }

    @Override
    public boolean enroll(Integer uid, String template) {
        Supplier<Boolean> supplier = () -> {
            boolean result = sdk.addTemplateStrToRam(template, uid);
            logger.info("enroll in-sdk fingerprint {} get result {}", uid, result);
            return result;
        };
        return invokeMethod(supplier);
    }

    @Override
    public boolean remove(Integer uid) {
        Supplier<Boolean> supplier = () -> {
            boolean result = sdk.removeUser(uid);
            logger.info("remove in-sdk fingerprint {} get result {}", uid, result);
            return result;
        };
        return invokeMethod(supplier);
    }

    @Override
    public int identify(String template) {
        Supplier<Integer> supplier = () -> {
            int result = sdk.verifyTemplateOneToManyFromStr(template);
            logger.info("identify in-sdk fingerprint get result {}", result);
            return result;
        };
        return invokeMethod(supplier);
    }

    /**
     * 检查本地运行环境
     */
    private void checkNativeEnv() {
        String osName = NativeUtil.getOsName();
        if (StringUtils.isEmpty(osName) || !osName.toLowerCase().contains(NativeConstant.OS_WINDOWS)) {
            logger.error("fingerprint sdk only support windows, but now is {}", osName);
            throw new BizException(BizResultEnum.FINGERPRINT_SDK_UNSUPPORTED_OS);
        }
        String jvmArch = NativeUtil.getJvmArch();
        if (StringUtils.isEmpty(jvmArch) || !jvmArch.equals(NativeConstant.ARCH_X86)) {
            logger.error("fingerprint sdk only support 32-bits jvm, but now is {}", jvmArch);
            throw new BizException(BizResultEnum.FINGERPRINT_SDK_UNSUPPORTED_JVM);
        }
    }

    /**
     * 在单线程池中调用指纹仪SDK方法并返回结果
     *
     * @param supplier 待执行任务
     * @return 执行结果
     */
    private <U> U invokeMethod(Supplier<U> supplier) {
        CompletableFuture<U> future = CompletableFuture.supplyAsync(supplier, executor);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("invoke fingerprint sdk method failed", e);
            throw new BizException(BizResultEnum.FINGERPRINT_SDK_INVOKE_METHOD_ERROR);
        }
    }

    /**
     * 创建SDK内部缓冲
     */
    private void createSdkCache() {
        Supplier<Integer> supplier = () -> {
            int result = sdk.createFPCacheDB();
            logger.info("create in-sdk cache get result {}", result);
            return result;
        };

        int result = invokeMethod(supplier);
        if (result != FingerprintConstant.SDK_CREATE_CACHE_SUCCESS) {
            logger.error("create in-sdk cache failed");
            throw new BizException(BizResultEnum.FINGERPRINT_SDK_CACHE_CREATE_ERROR);
        }
    }
}
