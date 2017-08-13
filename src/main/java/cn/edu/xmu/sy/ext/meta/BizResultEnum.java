/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

import cn.com.lx1992.lib.base.meta.IResultEnum;

/**
 * 业务结果码枚举
 *
 * @author luoxin
 * @version 2017-3-18
 */
public enum BizResultEnum implements IResultEnum {
    //用户Service错误码(201**)
    USER_MGR_DISABLE(20101, "用户管理功能被禁用."),
    USER_NUMBER_DUPLICATE(20102, "编号为{0}的用户已存在."),
    USER_CREATE_ERROR(20103, "创建用户失败."),
    USER_NOT_EXIST(20104, "ID为{0,number,#}的用户不存在."),
    USER_MODIFY_ERROR(20105, "修改用户失败."),
    USER_DELETE_ERROR(20106, "删除用户失败."),
    //指纹Service错误码(202**)
    FINGERPRINT_NOT_EXIST(20201, "ID为{0,number,#}的指纹不存在."),
    FINGERPRINT_FINGER_EXIST(20202, "手指{0}已经登记为其他指纹."),
    FINGERPRINT_MODIFY_ERROR(20203, "修改指纹失败."),
    FINGERPRINT_DELETE_ERROR(20204, "删除指纹失败."),
    FINGERPRINT_ENROLL_MAX_COUNT(20205, "此用户已登记{0}枚指纹."),
    FINGERPRINT_ENROLL_ERROR(20206, "登记指纹失败."),
    FINGERPRINT_UID_NOT_EXIST(20207, "UID为{0,number,#}的指纹不存在."),
    FINGERPRINT_IDENTIFY_ERROR(20208, "辨识指纹失败."),
    //指纹仪SDK Service错误码(203**)
    FINGERPRINT_SDK_UNSUPPORTED_OS(20301, "指纹仪SDK仅支持Windows操作系统."),
    FINGERPRINT_SDK_UNSUPPORTED_JVM(20302, "指纹仪SDK仅支持32位Java虚拟机."),
    FINGERPRINT_SDK_INVOKE_METHOD_ERROR(20303, "指纹仪SDK方法调用失败."),
    FINGERPRINT_SDK_CACHE_CREATE_ERROR(20304, "指纹仪SDK内部缓冲创建失败."),
    FINGERPRINT_SDK_TEMPLATE_DUPLICATE(20305, "此指纹已经登记过."),
    FINGERPRINT_SDK_ENROLL_ERROR(20306, "向指纹仪SDK登记指纹失败."),
    FINGERPRINT_SDK_REMOVE_ERROR(20317, "从指纹仪SDK移除指纹失败."),
    FINGERPRINT_SDK_IDENTIFY_ERROR(20308, "从指纹仪SDK辨识指纹失败."),
    //窗口Service错误码(204**)
    COUNTER_NUMBER_DUPLICATE(20401, "编号{0}与其他窗口重复."),
    COUNTER_ALREADY_BIND(20402, "MAC地址{0}和IP地址{1}已经绑定其他窗口."),
    COUNTER_CREATE_ERROR(20403, "窗口创建失败."),
    COUNTER_NOT_EXIST(20404, "ID为{0,number,#}的窗口不存在."),
    COUNTER_MODIFY_ERROR(20405, "窗口修改失败."),
    COUNTER_MODIFY_ONLINE(20406, "窗口存在在线会话时不能修改MAC地址或IP地址."),
    COUNTER_DELETE_ERROR(20407, "窗口删除失败."),
    COUNTER_UNBIND(20409, "MAC地址{0}和IP地址{1}尚未绑定任何窗口."),
    //会话Service错误码(205**)
    SESSION_ANOTHER_ONLINE(20502, "该窗口已有其他客户端在线."),
    SESSION_ONLINE_ERROR(20503, "会话上线失败."),
    SESSION_NOT_EXIST(20501,"ID为{0,number,#}的会话不存在"),
    SESSION_TOKEN_NOT_EXIST(20504, "Token为{0}的会话不存在."),
    SESSION_UPDATE_STATUS_ERROR(20506, "更新会话状态失败."),
    SESSION_TOKEN_INVALIDATE(20505, "Token为{0}的的会话已失效."),
    SESSION_DELETE_ONLINE(20506, "无法删除处于在线状态的会话."),
    SESSION_DELETE_ERROR(20506, "删除会话失败."),
    //消息错误码(206**)
    MESSAGE_TARGET_NOT_ONLINE(20603, "消息发送目标当前无在线会话."),
    MESSAGE_CREATE_ERROR(20604, "创建消息失败."),
    MESSAGE_UPDATE_ERROR(20604, "修改消息失败."),
    MESSAGE_DELETE_ERROR(20604, "删除消息失败."),
    //TODO 临时错误，合并HttpUtil
    MESSAGE_INVOKE_CALLBACK_ERROR(20605, "调用第三方接口失败."),
    MESSAGE_RECEIVE_UNKNOWN_TYPE(20606, "未知的回复消息类型."),
    MESSAGE_NOT_EXIST(20607, "ID为{0,number,#}的消息不存在."),
    MESSAGE_UID_NOT_EXIST(20607, "UID为{0,number,#}的消息不存在."),
    //WebSocket错误码(207**)
    WEB_SOCKET_TOKEN_NOT_EXIST(20701, "WebSocket连接中不存在Token."),
    WEB_SOCKET_CONNECTION_NOT_EXIST(20701, "WebSocket连接未建立或已关闭."),
    WEB_SOCKET_CONNECTION_IO_ERROR(20702, "WebSocket通信时发生I/O错误."),
    //数据同步Service错误码(208**)
    SYNC_USER_NUMBER_NOT_EXIST(20801, "编号为{0}的用户不存在"),
    //设置错误码(209**)
    SETTING_NOT_EXIST(20901, "分组{0}下没有可供设置的参数."),
    SETTING_NOT_MODIFY(20902, "设置未被修改，无需保存."),
    SETTING_VALUE_INVALID(20903, "设置项''{0}''取值未能通过校验，请检查."),
    SETTING_SAVE_ERROR(20904, "保存设置失败."),
    //资源错误码(210**)
    RESOURCE_CREATE_ERROR(21001, "资源创建失败."),
    RESOURCE_NOT_EXIST(21002, "ID为{0,number,#}的资源不存在."),
    RESOURCE_MODIFY_ERROR(21003, "资源修改失败."),
    RESOURCE_DELETE_ERROR(21004, "资源删除失败."),
    //TTS错误码(211**)
    TTS_IO_ERROR(21101, "语音合成I/O错误."),
    TTS_SDK_ERROR(21102, "语音合成SDK错误."),
    TTS_STATE_ERROR(21103, "语音合成任务状态错误."),
    //日志错误码(212**)
    LOG_CREATE_ERROR(21201, "创建日志失败.");

    private Integer code;
    private String message;

    BizResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
