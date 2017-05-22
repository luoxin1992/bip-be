/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

import java.util.List;

/**
 * 指纹登记Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollMessage extends BaseMessage {
    /**
     * 采集次数
     */
    private Integer times;
    /**
     * 图像/声音资源
     */
    private List<BaseMessageResource> resources;

    public FingerprintEnrollMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL);
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public List<BaseMessageResource> getResources() {
        return resources;
    }

    public void setResources(List<BaseMessageResource> resources) {
        this.resources = resources;
    }
}
