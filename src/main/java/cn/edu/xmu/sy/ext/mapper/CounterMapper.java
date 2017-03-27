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
 * 柜台Mapper
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

    List<CounterDO> listByParam(CounterQueryParam param);

    Long countByParam(CounterQueryParam param);

    Long getIdByNumber(@Param("number") String number, @Param("exclude") Long exclude);

    Long getIdByMacAndIp(@Param("mac") String mac, @Param("ip") String ip, @Param("exclude") Long exclude);
}
