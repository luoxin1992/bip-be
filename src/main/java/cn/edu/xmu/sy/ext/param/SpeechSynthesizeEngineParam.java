/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 语音合成引擎参数
 *
 * @author luoxin
 * @version 2017-4-17
 */
public class SpeechSynthesizeEngineParam extends BaseParam {
    /**
     * 发音人
     */
    private String voiceName;
    /**
     * 合成语速
     */
    private String speed;
    /**
     * 合成音量
     */
    private String volume;
    /**
     * 合成语调
     */
    private String pitch;

    public String getVoiceName() {
        return voiceName;
    }

    public void setVoiceName(String voiceName) {
        this.voiceName = voiceName;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }
}
