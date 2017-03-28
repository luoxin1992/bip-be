/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.ResourceDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.ResourceMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.result.ResourceItemQueryResult;
import cn.edu.xmu.sy.ext.result.ResourceQueryResult;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoxin
 * @version 2017-3-27
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    private final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private LogService logService;

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public ResourceQueryResult query() {
        //TODO 资源库维护中不能查询
        List<ResourceDO> domains = resourceMapper.listAll();
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("resource query result is empty");
            throw new BizException(BizResultEnum.RESOURCE_ITEM_NOT_EXIST);
        }

        List<ResourceItemQueryResult> items = new ArrayList<>(domains.size());
        for (ResourceDO domain : domains) {
            ResourceItemQueryResult item = POJOConvertUtil.convert(domain, ResourceItemQueryResult.class);
            items.add(item);
        }

        ResourceQueryResult result = new ResourceQueryResult();
        result.setResources(items);
        logger.info("query {} resource(s)", items.size());
        return result;
    }

    @Override
    @Transactional
    public void create(String type, String name, String path) {
        ResourceDO domain = new ResourceDO();
        domain.setType(type);
        domain.setName(name);
        domain.setPath(path);
        domain.setMd5(calcMd5(path));
        if (resourceMapper.save(domain) != 1) {
            logger.error("create resource {} failed", domain.getId());
        }

        logService.logResourceCreate();
        logger.info("create counter {} successfully", domain.getId());
    }

    @Override
    public Long getIdByTypeAndName(String type, String name) {
        return resourceMapper.getIdByTypeAndName(type, name);
    }

    /**
     * 计算文件MD5
     *
     * @param path 文件路径
     * @return 文件MD5
     */
    private String calcMd5(String path) {
        try {
            logger.info("calculate md5 for file {}", path);
            InputStream inputStream = Files.newInputStream(Paths.get(path));
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            logger.error("error calculating md5", e);
            throw new BizException(BizResultEnum.RESOURCE_CALCULATE_MD5_ERROR);
        }
    }
}
