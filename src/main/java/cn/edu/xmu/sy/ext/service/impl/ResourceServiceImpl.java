/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.cat.annotation.CatTransaction;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.DigestUtil;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.disconf.ServletConfigItem;
import cn.edu.xmu.sy.ext.domain.ResourceDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.ResourceMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.param.ResourceQueryParam;
import cn.edu.xmu.sy.ext.result.ResourceListResult;
import cn.edu.xmu.sy.ext.result.ResourceListTypeResult;
import cn.edu.xmu.sy.ext.result.ResourceQueryResult;
import cn.edu.xmu.sy.ext.result.ResourceQuerySimpleResult;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author luoxin
 * @version 2017-3-27
 */
@CatTransaction
@Service
public class ResourceServiceImpl implements ResourceService {
    private final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private LogService logService;

    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private ServletConfigItem servletConfig;

    @Override
    public BaseListResult<ResourceListResult> list() {
        Long count = resourceMapper.countAll();
        if (count == 0) {
            logger.warn("resource list result is empty");
            return new BaseListResult<>();
        }

        Integer rows = 100;
        Integer pages = Math.toIntExact(count % rows == 0 ? count / rows : count / rows + 1);
        logger.info("resource list contain {} result(s) and divide to {} page(s)", count, pages);

        List<ResourceListResult> all = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            List<ResourceDO> domains = resourceMapper.listAll((long) (i * rows), rows);
            List<ResourceListResult> results = domains.stream()
                    .map(domain -> {
                        ResourceListResult result = POJOConvertUtil.convert(domain, ResourceListResult.class);
                        result.setUrl(buildUrl(domain.getType(), domain.getFilename()));
                        return result;
                    })
                    .collect(Collectors.toList());
            all.addAll(results);
        }
        logger.info("list all {} resource(s)", all.size());

        BaseListResult<ResourceListResult> result = new BaseListResult<>();
        result.setTotal(all.size());
        result.setList(all);
        return result;
    }

    @Override
    public BaseListResult<ResourceListTypeResult> listType() {
        List<ResourceListTypeResult> results = Arrays.stream(ResourceTypeEnum.values())
                .filter(value -> !ResourceTypeEnum.UNKNOWN.equals(value))
                .map(value -> {
                    ResourceListTypeResult result = new ResourceListTypeResult();
                    result.setType(value.getType());
                    result.setDescription(value.getDescription());
                    return result;
                })
                .collect(Collectors.toList());
        logger.info("list {} resource type(s)", results.size());

        BaseListResult<ResourceListTypeResult> result = new BaseListResult<>();
        result.setTotal(results.size());
        result.setList(results);
        return result;
    }

    @Override
    public BasePagingResult<ResourceQueryResult> query(ResourceQueryParam param) {
        Long count = resourceMapper.countByParam(param);
        if (count == 0) {
            logger.warn("resource query result is empty");
            return new BasePagingResult<>();
        }

        List<ResourceDO> domains = resourceMapper.listByParam(param);
        List<ResourceQueryResult> results = domains.stream()
                .map(domain -> {
                    ResourceQueryResult result = POJOConvertUtil.convert(domain, ResourceQueryResult.class);
                    result.setType(ResourceTypeEnum.getDescriptionByType(domain.getType()));
                    result.setUrl(buildUrl(domain.getType(), domain.getFilename()));
                    result.setTimestamp(domain.getGmtModify());
                    return result;
                })
                .collect(Collectors.toList());
        logger.info("query {} of {} resource(s)", results.size(), count);

        BasePagingResult<ResourceQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    @Override
    public List<ResourceQuerySimpleResult> queryByType(String type) {
        List<ResourceDO> domains = resourceMapper.getByType(type);
        if (CollectionUtils.isEmpty(domains)) {
            logger.warn("resource query by type {} result is empty", type);
            return Collections.emptyList();
        }

        logger.info("query {} resource(s) with type {}", domains.size(), type);
        return domains.stream()
                .map(domain -> {
                    ResourceQuerySimpleResult result = POJOConvertUtil.convert(domain, ResourceQuerySimpleResult.class);
                    result.setUrl(buildUrl(domain.getType(), domain.getFilename()));
                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ResourceQuerySimpleResult> queryByTag(String type, String tag) {
        ResourceDO domain = resourceMapper.getByTag(type, tag);
        if (domain == null) {
            logger.warn("resource with type {} and tag {} not exist", type, tag);
            return Optional.empty();
        }

        ResourceQuerySimpleResult result = POJOConvertUtil.convert(domain, ResourceQuerySimpleResult.class);
        result.setUrl(buildUrl(domain.getType(), domain.getFilename()));
        return Optional.of(result);
    }

    @Override
    @Transactional
    public void create(String type, String tag, String filename) {
        ResourceDO domain = new ResourceDO();
        domain.setType(type);
        domain.setTag(tag);
        domain.setFilename(filename);
        domain.setMd5(DigestUtil.getFileMD5(buildPath(type, filename)));

        if (resourceMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create resource {} failed", domain.getId());
            throw new BizException(BizResultEnum.RESOURCE_CREATE_ERROR);
        }

        logService.logResourceCreate(domain.getId(), domain.getTag(), domain.getFilename());
        logger.info("create resource {}", domain.getId());
    }

    @Override
    @Transactional
    public void modify(Long id, String filename) {
        ResourceDO before = resourceMapper.getById(id);
        if (before == null) {
            logger.error("resource {} not exist", id);
            throw new BizException(BizResultEnum.RESOURCE_NOT_EXIST, id);
        }

        ResourceDO after = new ResourceDO();
        after.setId(id);
        after.setFilename(filename);
        after.setMd5(DigestUtil.getFileMD5(buildPath(before.getType(), filename)));

        if (resourceMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify resource {} failed", after.getId());
            throw new BizException(BizResultEnum.RESOURCE_MODIFY_ERROR);
        }

        logService.logResourceModify(after.getId(), POJOCompareUtil.compare(ResourceDO.class, before, after));
        logger.info("modify resource {}", after.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (resourceMapper.removeById(id) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete resource {} failed", id);
            throw new BizException(BizResultEnum.RESOURCE_DELETE_ERROR);
        }

        logService.logResourceDelete(id);
        logger.info("delete resource {}", id);
    }

    /**
     * 由类型和文件名构造其访问URL
     *
     * @param type     类型
     * @param filename 文件名
     * @return URL
     */
    private String buildUrl(String type, String filename) {
        //不需要返回上下文路径
        return CommonConstant.SLASH_STRING + type + CommonConstant.SLASH_STRING + filename;
    }

    /**
     * 由类型和文件名构造其访问路径
     *
     * @param type     类型
     * @param filename 文件名
     * @return 访问路径
     */
    private String buildPath(String type, String filename) {
        return servletConfig.getDocumentRoot() + File.separatorChar + type + File.separatorChar + filename;
    }
}
