/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.domain.ResourceDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.ResourceMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.param.ResourceCreateParam;
import cn.edu.xmu.sy.ext.param.ResourceModifyParam;
import cn.edu.xmu.sy.ext.param.ResourceQueryParam;
import cn.edu.xmu.sy.ext.result.ResourceListResult;
import cn.edu.xmu.sy.ext.result.ResourceQueryResult;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public BaseListResult<ResourceListResult> list() {
        List<ResourceDO> domains = resourceMapper.listAll();
        List<ResourceListResult> results = domains.stream()
                .map((domain) -> {
                    ResourceListResult result = POJOConvertUtil.convert(domain, ResourceListResult.class);
                    //TODO 拼接静态资源路径
                    result.setUrl(domain.getPath());
                    return result;
                })
                .collect(Collectors.toList());

        BaseListResult<ResourceListResult> result = new BaseListResult<>();
        result.setList(results);
        logger.info("list {} resource(s)", results.size());
        return result;
    }

    @Override
    public BasePagingResult<ResourceQueryResult> query(ResourceQueryParam param) {
        long count = resourceMapper.countByParam(param);
        List<ResourceDO> domains = resourceMapper.listByParam(param);
        List<ResourceQueryResult> results = domains.stream()
                .map((domain) -> {
                    ResourceQueryResult result = POJOConvertUtil.convert(domain, ResourceQueryResult.class);
                    result.setType(ResourceTypeEnum.getDescriptionByType(domain.getType()));
                    //TODO 拼接静态资源路径
                    result.setUrl(domain.getPath());
                    result.setTimestamp(domain.getGmtModify());
                    return result;
                })
                .collect(Collectors.toList());

        BasePagingResult<ResourceQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        logger.info("query {}/{} resource(s)", results.size(), count);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(ResourceCreateParam param) {
        ResourceDO domain = POJOConvertUtil.convert(param, ResourceDO.class);
        if (resourceMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create resource {} failed", domain.getId());
            throw new BizException(BizResultEnum.RESOURCE_CREATE_ERROR);
        }

        logService.logResourceCreate(domain);
        logger.info("create resource {} successfully", domain.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void modify(ResourceModifyParam param) {
        ResourceDO before = resourceMapper.getById(param.getId());
        if (before == null) {
            logger.error("resource {} not exist", param.getId());
            throw new BizException(BizResultEnum.RESOURCE_NOT_EXIST, param.getId());
        }
        ResourceDO domain = POJOConvertUtil.convert(param, ResourceDO.class);
        if (resourceMapper.updateById(domain) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify resource {} failed", domain.getId());
            throw new BizException(BizResultEnum.RESOURCE_MODIFY_ERROR);
        }

        logService.logResourceModify(before, domain);
        logger.info("modify resource {} successfully", domain.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void delete(Long id) {
        if (resourceMapper.removeById(id) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete resource {} failed", id);
            throw new BizException(BizResultEnum.RESOURCE_MODIFY_ERROR);
        }

        logService.logResourceDelete(id);
        logger.info("delete resource {} successfully", id);
    }

    @Override
    public Optional<Long> getIdByTypeAndName(String type, String name) {
        return Optional.ofNullable(resourceMapper.getIdByTypeAndName(type, name));
    }
}
