/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.CounterDO;
import org.apache.ibatis.annotations.Mapper;

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
}
