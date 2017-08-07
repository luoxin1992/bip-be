/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.dto;

/**
 * TTS任务参数
 *
 * @author luoxin
 * @version 2017-4-21
 */
public class TtsTaskParamDTO {
    /**
     * NUS模式
     */
    private int nus;
    /**
     * 语音采样率
     */
    private String sampleRate;
    /**
     * 语音格式类型
     */
    private String encodeType;
    /**
     * 发音人
     */
    private String voice;
    /**
     * 合成音量
     */
    private int volume;
    /**
     * 合成语调
     */
    private int pitchRate;
    /**
     * 合成语速
     */
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
