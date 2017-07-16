/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 更新公司信息Message
 *
 * @author luoxin
 * @version 2017-4-29
 */
public class UpdateCompanyInfoMessage extends BaseSendMessage {
    /**
     * 公司商标
     */
    private String logo;
    /**
     * 公司名称
     */
    private String name;

    public UpdateCompanyInfoMessage() {
        super(MessageTypeEnum.UPDATE_COUNTER_INFO);
    }

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
