/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.config;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import org.springframework.stereotype.Component;

/**
 * 依赖服务配置项
 * <p>
 * 所在主机
 * 内部API
 *
 * @author luoxin
 * @version 2017-5-11
 */
@Component
public class DependencyConfigItem {
    private String hostData;
    private String hostMsg;
    private String hostFp;

    private String apiDataFingerprintListTemplate;
    private String apiDataSessionLostClient;
    private String apiDataMessageReply;
    private String apiDataSyncMessageStatus;

    private String apiMsgTokenRegister;
    private String apiMsgTokenUnregister;
    private String apiMsgMessageSend;

    private String apiFpEnroll;
    private String apiFpRemove;
    private String apiFpIdentify;

    @DisconfItem(key = "dependency.service.data.host", app = "bip-common")
    public String getHostData() {
        return hostData;
    }

    public void setHostData(String hostData) {
        this.hostData = hostData;
    }

    @DisconfItem(key = "dependency.service.msg.host", app = "bip-common")
    public String getHostMsg() {
        return hostMsg;
    }

    public void setHostMsg(String hostMsg) {
        this.hostMsg = hostMsg;
    }

    @DisconfItem(key = "dependency.service.fp.host", app = "bip-common")
    public String getHostFp() {
        return hostFp;
    }

    public void setHostFp(String hostFp) {
        this.hostFp = hostFp;
    }

    @DisconfItem(key = "dependency.service.data.api.fingerprint.list.template", app = "bip-common")
    public String getApiDataFingerprintListTemplate() {
        return apiDataFingerprintListTemplate;
    }

    public void setApiDataFingerprintListTemplate(String apiDataFingerprintListTemplate) {
        this.apiDataFingerprintListTemplate = apiDataFingerprintListTemplate;
    }

    @DisconfItem(key = "dependency.service.data.api.session.lost.client", app = "bip-common")
    public String getApiDataSessionLostClient() {
        return apiDataSessionLostClient;
    }

    public void setApiDataSessionLostClient(String apiDataSessionLostClient) {
        this.apiDataSessionLostClient = apiDataSessionLostClient;
    }

    @DisconfItem(key = "dependency.service.data.api.message.reply", app = "bip-common")
    public String getApiDataMessageReply() {
        return apiDataMessageReply;
    }

    public void setApiDataMessageReply(String apiDataMessageReply) {
        this.apiDataMessageReply = apiDataMessageReply;
    }

    @DisconfItem(key = "dependency.service.data.api.sync.message.status", app = "bip-common")
    public String getApiDataSyncMessageStatus() {
        return apiDataSyncMessageStatus;
    }

    public void setApiDataSyncMessageStatus(String apiDataSyncMessageStatus) {
        this.apiDataSyncMessageStatus = apiDataSyncMessageStatus;
    }

    @DisconfItem(key = "dependency.service.msg.api.token.register", app = "bip-common")
    public String getApiMsgTokenRegister() {
        return apiMsgTokenRegister;
    }

    public void setApiMsgTokenRegister(String apiMsgTokenRegister) {
        this.apiMsgTokenRegister = apiMsgTokenRegister;
    }

    @DisconfItem(key = "dependency.service.msg.api.token.unregister", app = "bip-common")
    public String getApiMsgTokenUnregister() {
        return apiMsgTokenUnregister;
    }

    public void setApiMsgTokenUnregister(String apiMsgTokenUnregister) {
        this.apiMsgTokenUnregister = apiMsgTokenUnregister;
    }

    @DisconfItem(key = "dependency.service.msg.api.message.send", app = "bip-common")
    public String getApiMsgMessageSend() {
        return apiMsgMessageSend;
    }

    public void setApiMsgMessageSend(String apiMsgMessageSend) {
        this.apiMsgMessageSend = apiMsgMessageSend;
    }

    @DisconfItem(key = "dependency.service.fp.api.enroll", app = "bip-common")
    public String getApiFpEnroll() {
        return apiFpEnroll;
    }

    public void setApiFpEnroll(String apiFpEnroll) {
        this.apiFpEnroll = apiFpEnroll;
    }

    @DisconfItem(key = "dependency.service.fp.api.remove", app = "bip-common")
    public String getApiFpRemove() {
        return apiFpRemove;
    }

    public void setApiFpRemove(String apiFpRemove) {
        this.apiFpRemove = apiFpRemove;
    }

    @DisconfItem(key = "dependency.service.fp.api.identify", app = "bip-common")
    public String getApiFpIdentify() {
        return apiFpIdentify;
    }

    public void setApiFpIdentify(String apiFpIdentify) {
        this.apiFpIdentify = apiFpIdentify;
    }
}
