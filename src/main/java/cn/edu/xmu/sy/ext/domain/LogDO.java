/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.domain;

import cn.com.lx1992.lib.annotation.FieldComment;
import cn.com.lx1992.lib.base.domain.BaseDO;

/**
 * 日志Domain
 *
 * @author luoxin
 * @version 2017-3-9
 */
public class LogDO extends BaseDO {
    /**
     * 类别
     */
    @FieldComment("类别")
    private String category;
    /**
     * 内容
     */
    @FieldComment("内容")
    private String content;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
