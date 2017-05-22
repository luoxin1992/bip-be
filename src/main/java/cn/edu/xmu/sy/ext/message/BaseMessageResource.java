/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import java.util.List;

/**
 * 消息中的图像/声音资源
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseMessageResource {
    /**
     * 图像URI
     */
    private String image;
    /**
     * 声音URI(顺序播放列表)
     */
    private List<String> voices;

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
