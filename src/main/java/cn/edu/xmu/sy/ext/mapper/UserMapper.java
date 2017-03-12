/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.UserDO;
import cn.edu.xmu.sy.ext.param.UserQueryParam;
import cn.edu.xmu.sy.ext.result.UserQueryResult;
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
    Integer save(UserDO userDO);

    Integer saveBatch(List<UserDO> userDOs);

    Integer updateById(UserDO userDO);

    Integer removeById(Long id);

    UserQueryResult getById(Long id);

    List<UserQueryResult> listByKeyword(UserQueryParam param);

    Long countByKeyword(UserQueryParam param);
}
