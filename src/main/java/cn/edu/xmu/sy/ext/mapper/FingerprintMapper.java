/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 指纹Mapper
 *
 * @author luoxin
 * @version 2017-3-10
 */
@Mapper
public interface FingerprintMapper {
    Integer save(FingerprintDO domain);

    Integer saveBatch(List<FingerprintDO> domains);

    Integer updateById(FingerprintDO domain);

    Integer removeById(Long id);

    Integer removeByUserId(Long userId);

    FingerprintDO getById(Long id);

    FingerprintDO getByUid(Integer uid);

    Long countByUserId(Long userId);

    List<FingerprintDO> getByUserId(Long userId);

    List<FingerprintDO> listByUserId(List<Long> userIds);

    Long countAll();

    List<FingerprintDO> listAll(@Param("offset") Long offset, @Param("rows") Integer rows);
}
