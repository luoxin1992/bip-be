/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.DateTimeUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.FingerprintDO;
import cn.edu.xmu.sy.ext.mapper.FingerprintMapper;
import cn.edu.xmu.sy.ext.param.FingerprintBatchQueryParam;
import cn.edu.xmu.sy.ext.param.FingerprintQueryParam;
import cn.edu.xmu.sy.ext.result.FingerprintQueryResult;
import cn.edu.xmu.sy.ext.service.FingerprintService;
import cn.edu.xmu.sy.ext.service.LogService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author luoxin
 * @version 2017-3-23
 */
@Service
public class FingerprintServiceImpl implements FingerprintService {
    private final Logger logger = LoggerFactory.getLogger(FingerprintServiceImpl.class);

    @Autowired
    private LogService logService;

    @Autowired
    private FingerprintMapper fingerprintMapper;

    @Override
    public void enroll() {

    }

    @Override
    public void identify() {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByUserId(Long userId) {

    }

    @Override
    public List<FingerprintQueryResult> query(FingerprintQueryParam param) {
        List<FingerprintDO> domains = fingerprintMapper.getByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("fingerprint query result is empty");
            return Collections.emptyList();
        }
        List<FingerprintQueryResult> results = buildQueryResults(domains);
        logger.info("query {} fingerprints for user {}", results.size(), param.getUserId());
        return results;
    }


    @Override
    public List<FingerprintQueryResult> queryBatch(FingerprintBatchQueryParam param) {
        List<FingerprintDO> domains = fingerprintMapper.listByParam(param);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("fingerprint batch query result is empty");
            return Collections.emptyList();
        }
        List<FingerprintQueryResult> results = buildQueryResults(domains);
        logger.info("batch query {} fingerprints for {} users", results.size(), param.getUserIds().size());
        return results;
    }

    private List<FingerprintQueryResult> buildQueryResults(List<FingerprintDO> domains) {
        List<FingerprintQueryResult> results = new ArrayList<>();
        for (FingerprintDO domain : domains) {
            FingerprintQueryResult result = POJOConvertUtil.convert(domain, FingerprintQueryResult.class);
            if (domain.getEnrollTime() != null) {
                result.setEnrollTime(DateTimeUtil.format(domain.getEnrollTime()));
            }
            if (domain.getIdentifyTime() != null) {
                result.setIdentifyTime(DateTimeUtil.format(domain.getIdentifyTime()));
            }
            results.add(result);
        }
        return results;
    }
}
