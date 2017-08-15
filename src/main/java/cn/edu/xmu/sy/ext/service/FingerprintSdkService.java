/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

/**
 * 指纹仪SDK Service
 *
 * @author LuoXin
 * @version 2017/5/6
 */
public interface FingerprintSdkService {
    /**
     * 将指纹模板添加到内部缓冲
     *
     * @param uid                UID
     * @param template           模板
     * @param throwWhenDuplicate 模板重复时抛出异常
     */
    void enroll(Integer uid, String template, boolean throwWhenDuplicate);

    /**
     * 从内部缓冲移除指纹模板
     *
     * @param uid UID
     */
    void remove(Integer uid);

    /**
     * 从内部缓冲辨识指纹模板
     *
     * @param template 模板
     * @return UID
     */
    Integer identify(String template);
}
