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
    SPEECH("speech", null),
    SPEECH_VOICE_NAME("speech-voice-name", "xiaoyan"),
    SPEECH_SPEED("speech-speed", "50"),
    SPEECH_VOLUME("speech-volume", "80"),
    SPEECH_PITCH("speech-pitch", "50"),
    SPEECH_OUTPUT_DIR("speech-output-dir", "./files/voice"),
    FINGERPRINT("fingerprint", null),
    FINGERPRINT_MAX_ENROLL_COUNT("fingerprint-max-enroll-count", "1"),
    FINGERPRINT_ENROLL_TIMES("fingerprint-enroll-times", "3"),
    MISC("misc", null),
    MISC_USER_MGR_ENABLE("misc-user-mgr-enable", "false"),
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
