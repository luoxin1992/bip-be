/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 查询设置Result
 *
 * @author luoxin
 * @version 2017-3-27
 */
public class SettingListResult extends BaseResult {
    /**
     * 设置组
     */
    private List<SettingGroupListResult> groups;

    public List<SettingGroupListResult> getGroups() {
        return groups;
    }

    public void setGroups(List<SettingGroupListResult> groups) {
        this.groups = groups;
    }
}
