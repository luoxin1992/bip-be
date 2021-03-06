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
    //公司信息
    COMPANY("company", null),
    COMPANY_LOGO("company-logo", null),
    COMPANY_NAME("company-name", null),
    //语音合成
    TTS("tts", null),
    TTS_NUS("tts-nus", "1"),
    TTS_VOICE("tts-voice", "xiaoyun"),
    TTS_VOLUME("tts-volume", "100"),
    TTS_PITCH_RATE("tts-pitch-rate", "0"),
    TTS_SPEECH_RATE("tts-speech-rate", "0"),
    TTS_SAMPLE_RATE("tts-sample-rate", "16000"),
    TTS_ENCODE_TYPE("tts-encode-type", "wav"),
    //指纹识别
    FINGERPRINT("fingerprint", null),
    FINGERPRINT_ENROLL_MAX("fingerprint-enroll-max", "10"),
    FINGERPRINT_ENROLL_TIMES("fingerprint-enroll-times", "3"),
    FINGERPRINT_ENROLL_TIMEOUT("fingerprint-enroll-timeout", "60"),
    FINGERPRINT_IDENTIFY_TIMEOUT("fingerprint-identify-timeout", "30"),
    FINGERPRINT_IDENTIFY_CALLBACK("fingerprint-identify-callback", null);

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
