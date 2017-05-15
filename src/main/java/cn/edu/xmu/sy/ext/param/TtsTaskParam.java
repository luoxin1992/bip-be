/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * TTS SDK Param
 *
 * @author luoxin
 * @version 2017-4-21
 */
public class TtsTaskParam extends BaseParam {
    /**
     * NUS模式
     */
    @NotNull
    @Min(0)
    @Max(1)
    private int nus;
    /**
     * 语音采样率
     */
    @NotNull
    @Pattern(regexp = "8000|16000")
    private String sampleRate;
    /**
     * 语音格式类型
     */
    @NotNull
    @Pattern(regexp = "wav|mp3")
    private String encodeType;
    /**
     * 发音人
     */
    @NotNull
    @Pattern(regexp = "xiaoyun|xiaogang")
    private String voice;
    /**
     * 合成音量
     */
    @NotNull
    @Min(0)
    @Max(100)
    private int volume;
    /**
     * 合成语调
     */
    @NotNull
    @Min(-500)
    @Max(500)
    private int pitchRate;
    /**
     * 合成语速
     */
    @NotNull
    @Min(-500)
    @Max(500)
    private int speechRate;

    public int getNus() {
        return nus;
    }

    public void setNus(int nus) {
        this.nus = nus;
    }

    public String getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(String sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getEncodeType() {
        return encodeType;
    }

    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getPitchRate() {
        return pitchRate;
    }

    public void setPitchRate(int pitchRate) {
        this.pitchRate = pitchRate;
    }

    public int getSpeechRate() {
        return speechRate;
    }

    public void setSpeechRate(int speechRate) {
        this.speechRate = speechRate;
    }
}
