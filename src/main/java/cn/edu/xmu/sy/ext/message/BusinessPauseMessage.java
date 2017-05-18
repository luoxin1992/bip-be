/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

import java.util.List;

/**
 * 业务暂停受理Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BusinessPauseMessage extends BaseMessage {
    /**
     * 图片(URL)
     */
    private String image;
    /**
     * 声音(播放列表)(URL)
     */
    private List<String> voices;

    public BusinessPauseMessage() {
        super(MessageTypeEnum.BUSINESS_PAUSE);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getVoices() {
        return voices;
    }

    public void setVoices(List<String> voices) {
        this.voices = voices;
    }
}
