/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.param.FingerprintEnrollParam;
import cn.edu.xmu.sy.ext.param.FingerprintFingerListParam;
import cn.edu.xmu.sy.ext.param.FingerprintIdentifyParam;
import cn.edu.xmu.sy.ext.param.FingerprintModifyParam;
import cn.edu.xmu.sy.ext.result.FingerprintFingerListResult;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import cn.edu.xmu.sy.ext.result.FingerprintTemplateListResult;

import java.util.List;
import java.util.Map;

/**
 * 指纹Service
 *
 * @author luoxin
 * @version 2017-3-23
 */
public interface FingerprintService {
    /**
     * 修改指纹
     *
     * @param param 修改参数
     */
    void modify(FingerprintModifyParam param);

    /**
     * 删除指纹
     *
     * @param id 指纹ID
     */
    void delete(Long id);

    /**
     * 删除用户全部指纹
     *
     * @param userId 用户ID
     */
    void deleteByUser(Long userId);

    /**
     * 统计用户已登记指纹数
     *
     * @param userId 用户ID
     * @return 已登记指纹数
     */
    Long countByUser(Long userId);

    /**
     * 登记指纹
     *
     * @param param 登记参数
     */
    void enroll(FingerprintEnrollParam param);

    /**
     * 辨识指纹
     *
     * @param param 辨识参数
     * @return 用户ID(辨识结果)
     */
    Long identify(FingerprintIdentifyParam param);

    /**
     * 查询全部手指名称
     *
     * @return 查询结果
     */
    FingerprintFingerListResult listFinger();

    /**
     * 查询全部可用手指名称
     *
     * @param param 查询参数
     * @return 查询结果
     */
    FingerprintFingerListResult listFingerUsable(FingerprintFingerListParam param);

    /**
     * 查询全部指纹模型
     *
     * @return 查询结果
     */
    BaseListResult<FingerprintTemplateListResult> listTemplate();

    /**
     * 查询指纹
     *
     * @param userId 用户ID
     * @return 查询结果
     */
    List<FingerprintQueryResult> query(Long userId);

    /**
     * 批量查询指纹
     *
     * @param userIds 用户ID
     * @return 查询结果
     */
    List<FingerprintQueryResult> queryBatch(List<Long> userIds);

    /**
     * 批量查询指纹，并根据用户ID分组
     *
     * @param userIds 用户ID
     * @return 分组结果
     */
    Map<Long, List<FingerprintQueryResult>> queryBatchAndGroup(List<Long> userIds);
}
