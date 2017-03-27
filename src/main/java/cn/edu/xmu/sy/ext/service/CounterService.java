/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.edu.xmu.sy.ext.param.CounterAutoCreateParam;
import cn.edu.xmu.sy.ext.param.CounterCreateParam;
import cn.edu.xmu.sy.ext.param.CounterModifyParam;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import cn.edu.xmu.sy.ext.result.CounterQueryResult;

/**
 * 柜台Service
 *
 * @author luoxin
 * @version 2017-3-23
 */
public interface CounterService {
    /**
     * 创建柜台
     *
     * @param param 柜台信息
     */
    void create(CounterCreateParam param);

    /**
     * 自动创建柜台
     * 自动创建的柜台只有mac地址和ip地址
     *
     * @param param 柜台信息
     */
    void autoCreate(CounterAutoCreateParam param);

    /**
     * 编辑柜台
     *
     * @param param 柜台信息
     */
    void modify(CounterModifyParam param);

    /**
     * 删除柜台
     *
     * @param id 柜台ID
     */
    void delete(Long id);

    /**
     * 查询柜台信息
     *
     * @param param
     * @return
     */
    BasePagingResult<CounterQueryResult> query(CounterQueryParam param);

    /**
     * 根据MAC地址和IP地址查询柜台ID
     *
     * @param mac MAC地址
     * @param ip  IP地址
     * @return 柜台ID
     */
    Long getIdByMacAndIp(String mac, String ip);
}
