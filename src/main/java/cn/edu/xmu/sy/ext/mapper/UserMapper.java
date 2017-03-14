/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户Mapper
 *
 * @author luoxin
 * @version 2017-3-9
 */
@Mapper
public interface UserMapper {
    Integer save(UserDO domain);

    Integer saveBatch(List<UserDO> domains);

    Integer updateById(UserDO domain);

    Integer removeById(Long id);

    UserDO getById(Long id);

    //List<UserDO> listByParam(UserQueryParam param);

    //Long countByParam(UserQueryParam param);
}
