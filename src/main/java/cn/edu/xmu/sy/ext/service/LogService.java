/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.domain.CounterDO;
import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import cn.edu.xmu.sy.ext.domain.ResourceDO;
import cn.edu.xmu.sy.ext.domain.UserDO;
import cn.edu.xmu.sy.ext.param.LogQueryParam;
import cn.edu.xmu.sy.ext.result.LogCategoryListResult;
import cn.edu.xmu.sy.ext.result.LogQueryResult;

/**
 * 日志Service
 *
 * @author luoxin
 * @version 2017-3-24
 */
public interface LogService {
    /**
     * 查询日志
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<LogQueryResult> query(LogQueryParam param);

    /**
     * 查询日志类别
     *
     * @return 查询结果
     */
    LogCategoryListResult listCategory();

    /**
     * 记录“新增用户”操作日志
     *
     * @param user 用户信息
     */
    void logUserCreate(UserDO user);

    /**
     * 记录“修改用户”操作日志
     *
     * @param before 修改前用户信息
     * @param after  修改后用户信息
     */
    void logUserModify(UserDO before, UserDO after);

    /**
     * 记录“删除用户”操作日志
     *
     * @param id 用户ID
     */
    void logUserDelete(Long id);

    /**
     * 记录“新增指纹”操作日志
     *
     * @param fingerprint 指纹信息
     */
    void logFingerprintCreate(FingerprintDO fingerprint);

    /**
     * 记录“删除指纹(根据ID)”操作日志
     *
     * @param id 指纹ID
     */
    void logFingerprintDeleteById(Long id);

    /**
     * 记录“删除指纹(根据用户ID)”操作日志
     *
     * @param userId 用户ID
     */
    void logFingerprintDeleteByUserId(Long userId);

    /**
     * 记录“创建柜台”操作日志
     *
     * @param counter 柜台信息
     */
    void logCounterCreate(CounterDO counter);

    /**
     * 记录“修改柜台”操作日志
     *
     * @param before 修改前的柜台信息
     * @param after  修改后的柜台信息
     */
    void logCounterModify(CounterDO before, CounterDO after);

    /**
     * 记录“删除柜台”操作日志
     *
     * @param id 柜台ID
     */
    void logCounterDelete(Long id);

    void logSessionOnline();

    void logSessionOffline();

    /**
     * 记录“保存设置”操作日志
     *
     * @param description 描述
     * @param oldValue    旧值
     * @param newValue    新值
     */
    void logSettingSave(String description, String oldValue, String newValue);

    /**
     * 记录“创建资源”事件日志
     *
     * @param resource 资源信息
     */
    void logResourceCreate(ResourceDO resource);

    /**
     * 记录“修改资源”事件日志
     *
     * @param before 修改前的资源信息
     * @param after  修改后的资源信息
     */
    void logResourceModify(ResourceDO before, ResourceDO after);

    /**
     * 记录“删除资源”事件日志
     *
     * @param id 资源ID
     */
    void logResourceDelete(Long id);
}
