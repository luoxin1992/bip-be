/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * 用户同步动作枚举
 *
 * @author luoxin
 * @version 2017-4-19
 */
public enum UserSyncActionEnum {
    CREATE("create"),
    MODIFY("modify"),
    DELETE("delete");

    private String action;

    UserSyncActionEnum(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
