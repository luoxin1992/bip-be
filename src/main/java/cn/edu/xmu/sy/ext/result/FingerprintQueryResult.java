/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

/**
 * 指纹查询结果
 *
 * @author luoxin
 * @version 2017-3-11
 */
public class FingerprintQueryResult {
    private Long id;
    private String finger;
    private String template;
    private String enrollTime;
    private String identifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getEnrollTime() {
        return enrollTime;
    }

    public void setEnrollTime(String enrollTime) {
        this.enrollTime = enrollTime;
    }

    public String getIdentifyTime() {
        return identifyTime;
    }

    public void setIdentifyTime(String identifyTime) {
        this.identifyTime = identifyTime;
    }
}
