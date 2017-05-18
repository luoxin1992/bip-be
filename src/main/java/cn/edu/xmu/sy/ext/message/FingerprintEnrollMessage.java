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
    private Integer capture;
    /**
     * 图片(URI)
     */
    private List<String> images;
    /**
     * 声音(播放列表)(URI)
     */
    private List<List<String>> voices;

    public FingerprintEnrollMessage() {
        super(MessageTypeEnum.FINGERPRINT_ENROLL);
    }

    public Integer getCapture() {
        return capture;
    }

    public void setCapture(Integer capture) {
        this.capture = capture;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<List<String>> getVoices() {
        return voices;
    }

    public void setVoices(List<List<String>> voices) {
        this.voices = voices;
    }
}
