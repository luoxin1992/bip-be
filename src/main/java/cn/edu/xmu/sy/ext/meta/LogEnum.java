/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * 操作日志类别/内容枚举
 *
 * @author luoxin
 * @version 2017-4-18
 */
public enum LogEnum {
    USER_CREATE("创建用户", "创建了ID为{0,number,#}的用户[编号''{1}'', 姓名''{2}'']"),
    USER_MODIFY("修改用户", "修改了ID为{0,number,#}的用户[{1}]"),
    USER_DELETE("删除用户", "删除了ID为{0,number,#}的用户"),
    FINGERPRINT_CREATE("创建指纹", "为用户{0,number,#}创建了ID为{1}的指纹"),
    FINGERPRINT_DELETE("删除指纹", "删除了ID为{0,number,#}的指纹"),
    FINGERPRINT_DELETE_BY_USER("删除指纹", "删除了用户{0,number,#}的全部指纹"),
    COUNTER_CREATE("创建柜台", "创建了ID为{0,number,#}的柜台[编号''{1}'', 名称''{2}'']"),
    COUNTER_MODIFY("修改柜台", "修改了ID为{0,number,#}的柜台[{1}]"),
    COUNTER_DELETE("删除柜台", "删除了ID为{0,number,#}的柜台"),
    SETTING_SAVE("设置参数", "将参数''{0}''由''{1}''改为''{2}''"),
    RESOURCE_CREATE("创建资源", "创建了ID为{0,number,#}的资源[名称''{1}'', 文件名''{2}'']"),
    RESOURCE_MODIFY("修改资源", "修改了ID为{0,number,#}的资源[{1}]"),
    RESOURCE_DELETE("删除资源", "删除了ID为{0,number,#}的资源"),
    ;

    private String category;
    private String content;

    LogEnum(String category, String content) {
        this.category = category;
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }
}
