/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.CounterDO;
import cn.edu.xmu.sy.ext.param.CounterQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 窗口Mapper
 *
 * @author luoxin
 * @version 2017-3-14
 */
@Mapper
public interface CounterMapper {
    Integer save(CounterDO domain);

    Integer saveBatch(List<CounterDO> domains);

    Integer updateById(CounterDO domain);

    Integer removeById(Long id);

    CounterDO getById(Long id);

    CounterDO getByNumber(String number);

    CounterDO getByMacAndIp(@Param("mac") String mac, @Param("ip") String ip);

    List<CounterDO> listById(List<Long> counterIds);

    Long countByParam(CounterQueryParam param);

    List<CounterDO> listByParam(CounterQueryParam param);

    Long countAll();

    List<CounterDO> listAll(@Param("offset") Long offset, @Param("rows") Integer rows);
}
