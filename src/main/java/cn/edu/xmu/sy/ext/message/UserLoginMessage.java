/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 用户登录消息
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class UserLoginMessage extends BaseMessage {
    /**
     * 编号
     */
    private String number;
    /**
     * 姓名
     */
    private String name;
    /**
     * 照片URL
     */
    private String photo;

    public UserLoginMessage() {
        super(MessageTypeEnum.USER_LOGIN.getType());
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
