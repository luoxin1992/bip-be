/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.CounterCreateParam;
import cn.edu.xmu.sy.ext.param.CounterDeleteParam;
import cn.edu.xmu.sy.ext.param.CounterModifyParam;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;
import cn.edu.xmu.sy.ext.result.CounterQuerySimpleResult;

import java.util.Optional;

/**
 * 窗口Service
 *
 * @author luoxin
 * @version 2017-3-23
 */
public interface CounterService {
    /**
     * 创建窗口
     *
     * @param param 创建参数
     */
    void create(CounterCreateParam param);

    /**
     * 编辑窗口
     *
     * @param param 编辑参数
     */
    void modify(CounterModifyParam param);

    /**
     * 删除窗口
     *
     * @param param 删除参数
     */
    void delete(CounterDeleteParam param);

    /**
     * 查询窗口信息
     *
     * @param param 查询参数
     * @return 查询结果
     */
    BasePagingResult<CounterQueryResult> query(CounterQueryParam param);

    /**
     * 根据MAC地址和IP地址查询简版窗口信息
     *
     * @param mac MAC地址
     * @param ip  IP地址
     * @return 查询结果
     */
    CounterQuerySimpleResult querySimple(String mac, String ip);

    /**
     * 根据编号查询窗口ID
     *
     * @param number 编号
     * @return 窗口ID(可选值)
     */
    Optional<Long> getIdByNumberOptional(String number);
}
