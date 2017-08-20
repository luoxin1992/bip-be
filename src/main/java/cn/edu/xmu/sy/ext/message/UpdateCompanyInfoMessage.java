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
     * 名称
     */
    private String name;

    public UpdateCompanyInfoMessage() {
        super(MessageTypeEnum.UPDATE_COMPANY_INFO);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
