/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

import java.util.List;

/**
 * 业务受理成功Message
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BizSuccessMessage extends BaseMessage {
    /**
     * 附加内容
     */
    private String extra;
    /**
     * 图片(URL)
     */
    private String image;
    /**
     * 声音(播放列表)(URL)
     */
    private List<String> voices;

    public BizSuccessMessage() {
        super(MessageTypeEnum.BIZ_SUCCESS);
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
