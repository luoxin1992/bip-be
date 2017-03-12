/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.mapper;

import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 指纹Mapper
 *
 * @author luoxin
 * @version 2017-3-10
 */
@Mapper
public interface FingerprintMapper {
    Integer save(FingerprintDO fingerprintDO);

    Integer saveBatch(List<FingerprintDO> fingerprintDOs);

    Integer updateById(FingerprintDO fingerprintDO);

    Integer removeById(Long id);

    Integer removeByUserId(Long userId);

    FingerprintQueryResult getById(Long id);

    List<FingerprintQueryResult> getByUserId(Long userId);

    List<FingerprintQueryResult> listByUserId(List<Long> userIds);
}
