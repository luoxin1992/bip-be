/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service;

import cn.edu.xmu.sy.ext.param.FingerprintBatchQueryParam;
import cn.edu.xmu.sy.ext.param.FingerprintQueryParam;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;

import java.util.List;

/**
 * 指纹Service
 *
 * @author luoxin
 * @version 2017-3-23
 */
public interface FingerprintService {
    void enroll();

    void identify();

    void deleteById(Long id);

    void deleteByUserId(Long userId);

    List<FingerprintQueryResult> query(FingerprintQueryParam param);

    List<FingerprintQueryResult> queryBatch(FingerprintBatchQueryParam param);
}
