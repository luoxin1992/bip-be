/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

import cn.com.lx1992.lib.util.UIDGenerateUtil;
import cn.edu.xmu.sy.ext.meta.MessageTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送的消息基类
 *
 * @author luoxin
 * @version 2017-5-19
 */
public class BaseSendMessage extends BaseMessage {
    /**
     * 资源
     */
    private List<Resource> resources;

    BaseSendMessage(MessageTypeEnum type) {
        setUid(UIDGenerateUtil.Standard.nextId());
        setType(type.getType());
        setResources(new ArrayList<>());
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public static class Resource {
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
