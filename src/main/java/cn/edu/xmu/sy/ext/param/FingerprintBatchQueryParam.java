/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.annotation.ValidateRule;

import java.util.List;

/**
 * 批量查询指纹参数
 *
 * @author luoxin
 * @version 2017-3-25
 */
public class FingerprintBatchQueryParam {
    /**
     * 用户ID
     */
    @ValidateRule(comment = "用户ID")
    private List<Long> userIds;

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
}
