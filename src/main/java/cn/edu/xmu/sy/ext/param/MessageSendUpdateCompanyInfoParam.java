/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 更新窗口信息 消息发送Param
 *
 * @author luoxin
 * @version 2017-4-28
 */
public class MessageSendUpdateCompanyInfoParam extends MessageSendBaseParam {
    /**
     * 公司商标
     */
    @Size(min = 1, max = 16)
    @NotNull
    private String logo;
    /**
     * 公司名称
     */
    @Size(min = 1, max = 32)
    @NotNull
    private String name;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
