/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.SettingDO;
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

    Integer removeByParent(Long parent);

    SettingDO getById(Long id);

    String getValueByKey(String key);

    List<SettingDO> list();
}
