/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送的消息基类
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseSendMessage {
    /**
     * 消息ID
     */
    private Long uid;
    /**
     * 类型
     */
    private String type;
    /**
     * 资源
     */
    private Map<String, Resource> resources;

    BaseSendMessage(MessageTypeEnum type) {
        this.type = type.getType();
        this.resources = new HashMap<>();
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public void setResources(Map<String, Resource> resources) {
        this.resources = resources;
    }

    public static class Resource {
        /**
         * 文本
         */
        private String text;
        /**
         * 图像
         */
        private String image;
        /**
         * 声音(顺序播放列表)
         */
        private List<String> voices;

        public Resource() {
            voices = new ArrayList<>();
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
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
}
