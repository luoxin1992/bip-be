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
    //用户错误码(201**)
    USER_MGR_DISABLE(20101, "用户管理功能被禁用."),
    USER_NUMBER_DUPLICATE(20102, "编号为{0}的用户已存在."),
    USER_CREATE_ERROR(20103, "新增用户失败."),
    USER_NOT_EXIST(20104, "ID为{0,number,#}的用户不存在."),
    USER_MODIFY_ERROR(20105, "修改用户失败."),
    USER_DELETE_ERROR(20106, "删除用户失败."),
    //TODO
    USER_NUMBER_NOT_EXIST(20199, "编号为{0}的用户不存在."),
    //指纹错误码(202**)
    FINGERPRINT_NOT_EXIST(20201, "ID为{0,number,#}的指纹不存在."),
    FINGERPRINT_MODIFY_ERROR(20202, "修改指纹失败."),
    FINGERPRINT_DELETE_ERROR(20203, "删除指纹失败."),
    FINGERPRINT_USER_NOT_EXIST(20204, "此用户未登记过指纹"),
    FINGERPRINT_ENROLL_COUNT_OVERRUN(20205, "此用户已登记{0}枚指纹."),
    FINGERPRINT_ENROLL_ERROR(20206, "登记指纹失败."),
    FINGERPRINT_ALREADY_ENROLL(20207, "此指纹已登记过."),
    FINGERPRINT_IDENTIFY_MISMATCH(20208, "未能辨识指纹."),
    FINGERPRINT_NO_ENROLL(20209, "此指纹未登记过."),
    FINGERPRINT_IDENTIFY_ERROR(20210, "辨识指纹失败."),
    FINGERPRINT_NO_USABLE_FINGER(20211, "此用户无可登记手指."),
    FINGERPRINT_REMOTE_CALL_ERROR(20212, "下游Fp服务调用失败."),
    FINGERPRINT_REMOTE_SERVICE_ERROR(20213, "下游Fp服务调用错误: {0,number,#}."),
    //窗口错误码(203**)
    COUNTER_NUMBER_DUPLICATE(20301, "编号{0}与其他窗口重复."),
    COUNTER_MAC_OR_IP_DUPLICATE(20302, "MAC地址{0}或IP地址{1}与其他窗口重复."),
    COUNTER_CREATE_ERROR(20303, "窗口创建失败."),
    COUNTER_NOT_EXIST(20304, "ID为{0,number,#}的窗口不存在."),
    COUNTER_MODIFY_ERROR(20305, "窗口修改失败."),
    COUNTER_ONLINE_SESSION_EXIST(20306, "窗口存在在线会话，不能删除."),
    COUNTER_DELETE_ERROR(20307, "窗口删除失败."),
    //会话错误码(204**)
    SESSION_COUNTER_UNBIND(20401, "客户端尚未与窗口绑定，请运行客户端初始化工具，或通过管理后台创建窗口，然后重新启动客户端."),
    SESSION_ANOTHER_ONLINE(20402, "该窗口已有其他客户端在线，如遇非正常离线，请通过管理后台操作强制关闭，或40秒后重试."),
    SESSION_ONLINE_ERROR(20403, "会话上线失败."),
    SESSION_TOKEN_NOT_EXIST(20404, "Token为{0}的会话不存在."),
    SESSION_NOT_ONLINE(20405, "ID为{0,number,#}的会话非在线状态."),
    SESSION_UPDATE_STATUS_ERROR(20406, "更新会话状态失败."),
    SESSION_NOT_EXIST(20407, "ID为{0,number,#}的会话不存在."),
    //消息错误码(205**)
    MESSAGE_SEND_TO_NULL(20501, "消息发送目标为空."),
    MESSAGE_SEND_TO_COUNTER_NOT_EXIST(20502, "消息发送目标窗口不存在."),
    MESSAGE_SEND_TO_SESSION_NOT_ONLINE(20503, "消息发送目标窗口当前无在线会话."),
    MESSAGE_CREATE_ERROR(20504, "创建消息失败."),
    MESSAGE_REPLY_TIMEOUT(20505, "等待消息回复超时."),
    MESSAGE_REPLY_UNKNOWN_TYPE(20506, "未知的回复消息类型."),
    MESSAGE_REPLY_NON_PENDING(20507, "ID为{0,number,#}的消息非阻塞型消息或当前非阻塞状态."),
    MESSAGE_REPLY_NO_EXIST(20508, "ID为{0,number,#}的消息回复不存在."),
    MESSAGE_REMOTE_CALL_ERROR(20509, "下游Msg服务调用失败."),
    MESSAGE_REMOTE_SERVICE_ERROR(20510, "下游Msg服务调用错误: {0,number,#}."),
    //设置错误码(206**)
    SETTING_NOT_EXIST(20601, "没有可供设置的参数."),
    SETTING_NOT_MODIFY(20602, "设置未被修改，无需保存."),
    SETTING_VALIDATE_FAIL(20603, "设置值未能通过校验，请检查."),
    SETTING_SAVE_ERROR(20604, "保存设置失败."),
    //资源错误码(207**)
    RESOURCE_CREATE_ERROR(20701, "资源创建失败."),
    RESOURCE_NOT_EXIST(20702, "ID为{0,number,#}的资源不存在."),
    RESOURCE_MODIFY_ERROR(20703, "资源修改失败."),
    RESOURCE_DELETE_ERROR(20704, "资源删除失败."),
    RESOURCE_NAME_NOT_EXIST(20705, "名称为{0}的资源不存在."),
    //日志错误码(208**)
    LOG_CREATE_ERROR(20801, "新增日志失败."),
    //TTS错误码(209**)
    TTS_IO_ERROR(20901, "语音合成I/O错误."),
    TTS_SDK_ERROR(20902, "语音合成SDK错误."),
    TTS_STATE_ERROR(20903, "语音合成状态错误."),
    TTS_UNKNOWN_ERROR(20904, "语音合成未知错误."),
    //跨服务返回码
    SERVICE_FP_TEMPLATE_DUPLICATE(40108, ""),
    SERVICE_FP_IDENTIFY_ERROR(40111, "");

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
