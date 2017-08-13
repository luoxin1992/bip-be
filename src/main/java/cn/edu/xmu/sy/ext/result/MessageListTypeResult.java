/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 查询全部消息类型Result
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class MessageListTypeResult extends BaseResult {
    /**
     * 类型
     */
    private String type;
    /**
     * 描述
     */
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
