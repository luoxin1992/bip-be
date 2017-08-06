/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.SettingDO;
import cn.edu.xmu.sy.ext.param.SettingQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设置Mapper
 *
 * @author luoxin
 * @version 2017-3-14
 */
@Mapper
public interface SettingMapper {
    Integer save(SettingDO domain);

    Integer saveBatch(List<SettingDO> domains);

    Integer updateById(SettingDO domain);

    Integer removeById(Long id);

    Integer removeByParentId(Long parent);

    SettingDO getById(Long id);

    SettingDO getByKey(String key);

    List<SettingDO> listByParam(SettingQueryParam param);
}
