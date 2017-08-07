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
    //用户日志
    USER_CREATE("新增用户", "新增了ID为{0,number,#}的用户[编号''{1}'', 姓名''{2}'']"),
    USER_MODIFY("修改用户", "修改了ID为{0,number,#}的用户[{1}]"),
    USER_DELETE("删除用户", "删除了ID为{0,number,#}的用户"),
    //指纹日志
    FINGERPRINT_MODIFY("修改指纹", "修改了ID为{0,number,#}的指纹[{1}]"),
    FINGERPRINT_DELETE("删除指纹", "删除了ID为{0,number,#}的指纹"),
    FINGERPRINT_DELETE_BY_USER("删除指纹", "删除了用户{0,number,#}的全部指纹"),
    FINGERPRINT_ENROLL("登记指纹", "登记了ID为{1}的指纹[用户ID{1,number,#}, 指纹UID''{2,number,#}'']]"),
    FINGERPRINT_IDENTIFY("辨识指纹", "辨识了ID为{0,number,#}的指纹[用户ID{1,number,#}, 指纹UID''{2,number,#}'']"),
    //窗口日志
    COUNTER_CREATE("新增窗口", "新增了ID为{0,number,#}的窗口[编号''{1}'', 名称''{2}'']"),
    COUNTER_MODIFY("修改窗口", "修改了ID为{0,number,#}的窗口[{1}]"),
    COUNTER_DELETE("删除窗口", "删除了ID为{0,number,#}的窗口"),
    //会话日志
    SESSION_CREATE("新增会话", "新增了ID为{0,number,#}的会话[窗口ID''{1,number,#}'', Token''{2}'']"),
    SESSION_STATUS_UPDATE("修改会话", "ID为{0,number,#}的会话状态变更为''{1}''[Token''{2}'']"),
    SESSION_DELETE_BY_COUNTER("删除会话", "删除了窗口{0,number,#}的全部会话"),
    //消息日志
    MESSAGE_SEND("发送消息", "发送了ID为{0,number,#}的消息[窗口ID''{1,number,#}'', 会话ID''{2,number,#}'', 消息UID''{3,number,#}'', 类型''{4}'', 长度''{5}'']"),
    MESSAGE_RECEIVE("接收消息", "接收了ID为{0,number,#}的消息[消息UID''{1,number,#}'', 类型''{2}'', 长度''{3}'']"),
    MESSAGE_DELETE_BY_COUNTER("删除消息", "删除了窗口{0,number,#}的全部消息"),
    //设置日志
    SETTING_SAVE("修改参数", "参数''{0}''的值由''{1}''变更为为''{2}''"),
    //资源日志
    RESOURCE_CREATE("新增资源", "新增了ID为{0,number,#}的资源[标签''{1}'', 文件名''{2}'']"),
    RESOURCE_MODIFY("修改资源", "修改了ID为{0,number,#}的资源[{1}]"),
    RESOURCE_DELETE("删除资源", "删除了ID为{0,number,#}的资源"),;

    private String type;
    private String content;

    LogEnum(String category, String content) {
        this.type = category;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
