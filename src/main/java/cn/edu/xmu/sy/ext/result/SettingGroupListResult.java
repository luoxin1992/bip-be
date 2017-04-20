/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 查询设置组Result
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingGroupListResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 描述
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 设置项
     */
    private List<SettingItemListResult> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<SettingItemListResult> getItems() {
        return items;
    }

    public void setItems(List<SettingItemListResult> items) {
        this.items = items;
    }
}
