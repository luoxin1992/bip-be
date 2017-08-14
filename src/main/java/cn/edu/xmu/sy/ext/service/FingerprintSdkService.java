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
     * 检查指纹模板是否已经存在于内部缓冲
     *
     * @param template 模板
     * @return -1表示不存在，正数表示已存在的UID
     */
    int check(String template);

    /**
     * 将指纹模板登记到内部缓冲
     *
     * @param uid      UID
     * @param template 模板
     * @return true-登记成功，false-登记失败
     */
    boolean enroll(Integer uid, String template);

    /**
     * 从内部缓冲移除指纹模板
     *
     * @param uid UID
     * @return true-移除成功，false-移除失败
     */
    boolean remove(Integer uid);

    /**
     * 从内部缓冲辨识指纹模板
     *
     * @param template 模板
     * @return -1表示辨识失败，正数表示辨识的UID
     */
    int identify(String template);
}
