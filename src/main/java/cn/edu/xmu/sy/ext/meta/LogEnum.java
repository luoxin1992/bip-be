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
    USER_CREATE("用户创建", "创建了ID为{0,number,#}的用户[编号''{1}'', 姓名''{2}'']"),
    USER_MODIFY("用户修改", "修改了ID为{0,number,#}的用户[{1}]"),
    USER_DELETE("用户删除", "删除了ID为{0,number,#}的用户"),
    //指纹日志
    FINGERPRINT_MODIFY("指纹修改", "修改了ID为{0,number,#}的指纹[{1}]"),
    FINGERPRINT_DELETE("指纹删除", "删除了ID为{0,number,#}的指纹"),
    FINGERPRINT_DELETE_BY_USER("指纹删除", "删除了用户{0,number,#}的全部指纹"),
    FINGERPRINT_CREATE("指纹登记", "登记了ID为{1}的指纹[用户ID{1,number,#}]"),
    FINGERPRINT_IDENTIFY("指纹辨识", "辨识了ID为{0,number,#}的指纹[用户ID{1,number,#}]"),
    //窗口日志
    COUNTER_CREATE("窗口创建", "创建了ID为{0,number,#}的窗口[编号''{1}'', 名称''{2}'']"),
    COUNTER_MODIFY("窗口修改", "修改了ID为{0,number,#}的窗口[{1}]"),
    COUNTER_DELETE("窗口删除", "删除了ID为{0,number,#}的窗口"),
    //会话日志
    SESSION_ONLINE("会话上线", "ID为{0,number,#}的会话上线[窗口ID''{1,number,#}'', Token''{2}'']"),
    SESSION_OFFLINE("会话下线", "ID为{0,number,#}的会话下线[Token''{1}'']"),
    SESSION_LOST_SERVER("会话服务端失联", "ID为{0,number,#}的会话与服务端失联[Token''{1}'']"),
    SESSION_LOST_CLIENT("会话客户端失联", "ID为{0,number,#}的会话与客户端失联[Token''{1}'']"),
    SESSION_CLOSE("会话强制关闭", "ID为{0,number,#}的会话被强制关闭"),
    //消息日志
    MESSAGE_SEND("消息发送", "发送了ID为{0,number,#}的消息[窗口ID''{1,number,#}'', 会话ID''{2,number,#}'', 类型''{3}'', 长度''{4}'']"),
    MESSAGE_RECEIVE("消息接收", "接收了ID为{0,number,#}的消息[回复ID''{1,number,#}'', 类型''{2}'', 长度''{3}'']"),
    //设置日志
    SETTING_SAVE("参数设置", "将参数''{0}''由''{1}''改为''{2}''"),
    //资源日志
    RESOURCE_CREATE("资源创建", "创建了ID为{0,number,#}的资源[名称''{1}'', 文件名''{2}'']"),
    RESOURCE_MODIFY("资源修改", "修改了ID为{0,number,#}的资源[{1}]"),
    RESOURCE_DELETE("资源删除", "删除了ID为{0,number,#}的资源"),;

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
