/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.meta;

/**
 * 参数设置枚举
 *
 * @author luoxin
 * @version 2017-3-27
 */
public enum SettingEnum {
    SPEECH("speech", "语音合成设置"),
    SPEECH_VOICE_NAME("speech-voice-name", "发音人"),
    SPEECH_SPEED("speech-speed", "合成语速"),
    SPEECH_VOLUME("speech-volume", "合成音量"),
    SPEECH_PITCH("speech-pitch", "合成语调"),
    SPEECH_OUTPUT_DIR("speech-output-dir", "输出目录"),
    ;

    private String key;
    private String description;

    SettingEnum(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
