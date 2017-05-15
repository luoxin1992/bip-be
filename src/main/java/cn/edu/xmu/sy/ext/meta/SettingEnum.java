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
    //语音合成设置
    TTS("tts", null),
    TTS_NUS("tts-nus", "1"),
    TTS_SAMPLE_RATE("tts-sample-rate", "16000"),
    TTS_ENCODE_TYPE("tts-encode-type", "wav"),
    TTS_VOICE("tts-voice", "xiaoyun"),
    TTS_VOLUME("tts-volume", "100"),
    TTS_PITCH_RATE("tts-pitch-rate", "0"),
    TTS_SPEECH_RATE("tts-speech-rate", "0"),
    //指纹识别设置
    FINGERPRINT("fingerprint", null),
    FINGERPRINT_MAX_ENROLL_COUNT("fingerprint-max-enroll-count", "1"),
    FINGERPRINT_ENROLL_TIMES("fingerprint-enroll-times", "3"),
    //其他设置
    MISC("misc", null),
    MISC_USER_MGR_ENABLE("misc-user-mgr-enable", "false"),
    MISC_DATA_MGR_ENABLE("misc-data-mgr-enable", "false"),
    ;

    /**
     * 键
     */
    private String key;
    /**
     * 默认值
     */
    private String defaultValue;

    SettingEnum(String key, String value) {
        this.key = key;
        this.defaultValue = value;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
