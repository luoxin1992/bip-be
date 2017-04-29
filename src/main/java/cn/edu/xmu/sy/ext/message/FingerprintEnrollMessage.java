/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

/**
 * 指纹登记Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class FingerprintEnrollMessage extends BaseMessage {
    /**
     * 手指名称
     */
    private String finger;
    /**
     * 指纹采集次数
     */
    private int captureTimes;

    public FingerprintEnrollMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL);
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public int getCaptureTimes() {
        return captureTimes;
    }

    public void setCaptureTimes(int captureTimes) {
        this.captureTimes = captureTimes;
    }
}
