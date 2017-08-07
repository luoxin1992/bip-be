/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.edu.xmu.sy.ext.param.FingerprintDeleteParam;
import cn.edu.xmu.sy.ext.param.FingerprintListFingerParam;
import cn.edu.xmu.sy.ext.param.FingerprintModifyParam;
import cn.edu.xmu.sy.ext.param.FingerprintQueryParam;
import cn.edu.xmu.sy.ext.result.FingerprintListFingerResult;
import cn.edu.xmu.sy.ext.result.FingerprintListTemplateResult;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;

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
     * @param param 删除参数
     */
    void delete(FingerprintDeleteParam param);

    /**
     * 删除用户全部指纹
     *
     * @param userId 用户ID
     */
    void deleteByUser(Long userId);

    /**
     * 登记指纹
     *
     * @param userId   用户ID
     * @param finger   手指
     * @param template 模板
     */
    void enroll(Long userId, String finger, String template);

    /**
     * 辨识指纹
     *
     * @param template 模板
     * @return 用户ID
     */
    Long identify(String template);

    /**
     * 查询可用手指名称
     *
     * @param param 查询参数
     * @return 查询结果
     */
    FingerprintListFingerResult listFinger(FingerprintListFingerParam param);

    /**
     * 查询全部指纹模型
     *
     * @return 查询结果
     */
    BaseListResult<FingerprintListTemplateResult> listTemplate();

    /**
     * 查询指纹
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BaseListResult<FingerprintQueryResult> query(FingerprintQueryParam param);

    /**
     * 批量查询指纹，根据用户ID分组
     *
     * @param userIds 用户ID
     * @return 分组结果
     */
    Map<Long, List<FingerprintQueryResult>> queryBatchAndGroup(List<Long> userIds);
}
