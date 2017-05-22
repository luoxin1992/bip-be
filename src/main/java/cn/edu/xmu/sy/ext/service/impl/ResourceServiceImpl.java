/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.service.impl;

import cn.com.lx1992.lib.base.result.BaseListResult;
import cn.com.lx1992.lib.base.result.BasePagingResult;
import cn.com.lx1992.lib.constant.CommonConstant;
import cn.com.lx1992.lib.util.POJOCompareUtil;
import cn.com.lx1992.lib.util.POJOConvertUtil;
import cn.edu.xmu.sy.ext.config.ServletConfigItem;
import cn.edu.xmu.sy.ext.domain.ResourceDO;
import cn.edu.xmu.sy.ext.exception.BizException;
import cn.edu.xmu.sy.ext.mapper.ResourceMapper;
import cn.edu.xmu.sy.ext.meta.BizResultEnum;
import cn.edu.xmu.sy.ext.meta.ResourceTypeEnum;
import cn.edu.xmu.sy.ext.param.ResourceCreateParam;
import cn.edu.xmu.sy.ext.param.ResourceModifyParam;
import cn.edu.xmu.sy.ext.param.ResourceQueryParam;
import cn.edu.xmu.sy.ext.result.ResourceListSimpleResult;
import cn.edu.xmu.sy.ext.result.ResourceQueryResult;
import cn.edu.xmu.sy.ext.service.LogService;
import cn.edu.xmu.sy.ext.service.ResourceService;
import cn.edu.xmu.sy.ext.service.TtsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
    private TtsService ttsService;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ServletConfigItem servletConfigItem;

    @Override
    public BaseListResult<ResourceListSimpleResult> listSimple() {
        String contextPath = servletConfigItem.getContextPath();
        String uriPrefix = (StringUtils.isEmpty(contextPath) || CommonConstant.SLASH_STRING.equals(contextPath)) ?
                CommonConstant.EMPTY_STRING : contextPath + CommonConstant.SLASH_STRING;

        List<ResourceDO> domains = listAllByTypeRaw(null);
        List<ResourceListSimpleResult> results = domains.stream()
                .map(domain -> {
                    ResourceListSimpleResult result = POJOConvertUtil.convert(domain, ResourceListSimpleResult.class);
                    result.setUri(uriPrefix + domain.getType() + CommonConstant.SLASH_STRING + domain.getFilename());
                    return result;
                })
                .collect(Collectors.toList());

        BaseListResult<ResourceListSimpleResult> resResult = new BaseListResult<>();
        resResult.setTotal(results.size());
        resResult.setList(results);
        return resResult;
    }

    @Override
    public BasePagingResult<ResourceQueryResult> query(ResourceQueryParam param) {
        Long count = resourceMapper.countByParam(param);
        if (count == 0) {
            logger.warn("resource query result is empty");
            return new BasePagingResult<>();
        }

        String contextPath = servletConfigItem.getContextPath();
        String uriPrefix = (StringUtils.isEmpty(contextPath) || CommonConstant.SLASH_STRING.equals(contextPath)) ?
                CommonConstant.EMPTY_STRING : contextPath + CommonConstant.SLASH_STRING;

        List<ResourceDO> domains = resourceMapper.listByParam(param);
        List<ResourceQueryResult> results = domains.stream()
                .map((domain) -> {
                    ResourceQueryResult result = POJOConvertUtil.convert(domain, ResourceQueryResult.class);
                    result.setType(ResourceTypeEnum.getDescriptionByType(domain.getType()));
                    result.setUri(uriPrefix + domain.getType() + CommonConstant.SLASH_STRING + domain.getFilename());
                    result.setTimestamp(domain.getGmtModify());
                    return result;
                })
                .collect(Collectors.toList());
        logger.info("query {}/{} resource(s)", results.size(), count);

        BasePagingResult<ResourceQueryResult> result = new BasePagingResult<>();
        result.setTotal(count);
        result.setPage(results);
        return result;
    }

    @Override
    @Transactional
    public void create(ResourceCreateParam param) {
        ResourceDO domain = POJOConvertUtil.convert(param, ResourceDO.class);
        if (resourceMapper.save(domain) != CommonConstant.SAVE_DOMAIN_SUCCESSFUL) {
            logger.error("create resource {} failed", domain.getId());
            throw new BizException(BizResultEnum.RESOURCE_CREATE_ERROR);
        }

        logService.logResourceCreate(domain.getId(), domain.getName(), domain.getFilename());
        logger.info("create resource {} successfully", domain.getId());
    }

    @Override
    @Transactional
    public void modify(ResourceModifyParam param) {
        ResourceDO before = resourceMapper.getById(param.getId());
        if (before == null) {
            logger.error("resource {} not exist", param.getId());
            throw new BizException(BizResultEnum.RESOURCE_NOT_EXIST, param.getId());
        }

        ResourceDO after = POJOConvertUtil.convert(param, ResourceDO.class);
        if (resourceMapper.updateById(after) != CommonConstant.UPDATE_DOMAIN_SUCCESSFUL) {
            logger.error("modify resource {} failed", after.getId());
            throw new BizException(BizResultEnum.RESOURCE_MODIFY_ERROR);
        }

        logService.logResourceModify(after.getId(), POJOCompareUtil.compare(ResourceDO.class, before, after));
        logger.info("modify resource {} successfully", after.getId());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (resourceMapper.removeById(id) != CommonConstant.REMOVE_DOMAIN_SUCCESSFUL) {
            logger.error("delete resource {} failed", id);
            throw new BizException(BizResultEnum.RESOURCE_DELETE_ERROR);
        }

        logService.logResourceDelete(id);
        logger.info("delete resource {} successfully", id);
    }

    @Override
    public Optional<Long> getIdByTypeAndName(String type, String name) {
        return Optional.ofNullable(resourceMapper.getIdByTypeAndName(type, name));
    }

    @Override
    public List<String> getUriByTypeAndName(ResourceTypeEnum type, List<String> names) {
        List<String> uris = new ArrayList<>();

        names.forEach(name -> {
            Long id = resourceMapper.getIdByTypeAndName(type.getType(), name);
            if (id == null) {
                //请求的资源未找到时，图片资源将抛出异常，声音资源将自动合成
                switch (type) {
                    case IMAGE:
                        logger.error("image resource {} not exist", name);
                        throw new BizException(BizResultEnum.RESOURCE_NAME_NOT_EXIST, name);
                    case VOICE:
                        String path = ttsService.ttsSync(null, name, false);
                        String filename = path.substring(path.lastIndexOf(File.separatorChar) + 1);
                        uris.add(buildUri(type.getType(), filename));
                        break;
                }
            } else {
                ResourceDO domain = resourceMapper.getById(id);
                uris.add(buildUri(domain.getType(), domain.getFilename()));
            }
        });
        return uris;
    }

    @Override
    public void rebuildAllVoice() {
        List<ResourceDO> domains = listAllByTypeRaw(ResourceTypeEnum.VOICE.getType());
        List<String> names = domains.stream()
                .map(ResourceDO::getName)
                .collect(Collectors.toList());

        logger.info("{} resource(s) will rebuild", names.size());
        ttsService.ttsBatchAsync(null, names, true);
    }

    /**
     * 查询全部资源，可根据类型过滤，内部实现分页，返回原生Domain
     *
     * @param type 类型
     * @return 查询结果
     */
    private List<ResourceDO> listAllByTypeRaw(String type) {
        Long count = resourceMapper.countAll(type);
        if (count == 0) {
            logger.warn("resource list all result is empty");
            return Collections.emptyList();
        }

        Integer rows = 100;
        Integer pages = Math.toIntExact(count % rows == 0 ? count / rows : count / rows + 1);
        logger.info("resource list all contain {} result(s) and divide to {} page(s)", count, pages);

        List<ResourceDO> allDomains = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            List<ResourceDO> domains = resourceMapper.listByPaging(type, (long) (i * rows), rows);
            allDomains.addAll(domains);
        }
        logger.info("list all {} resource(s) with type {}", allDomains.size(), type);
        return allDomains;
    }

    /**
     * 根据资源文件的类型和文件名构建访问其的URI
     *
     * @param type     类型
     * @param filename 文件名
     * @return URI
     */
    private String buildUri(String type, String filename) {
        String contextPath = servletConfigItem.getContextPath();
        String uriPrefix = (StringUtils.isEmpty(contextPath) || CommonConstant.SLASH_STRING.equals(contextPath)) ?
                CommonConstant.EMPTY_STRING : contextPath + CommonConstant.SLASH_STRING;
        return uriPrefix + type + CommonConstant.SLASH_STRING + filename;
    }
}
