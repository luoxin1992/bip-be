/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.dto;

/**
 * @author luoxin
 * @version 2017-8-12
 */
public class FingerprintIdentifyCallbackDTO {
    /**
     * 编号
     */
    private String number;
    /**
     * 姓名
     */
    private String name;
    /**
     * 照片(URL)
     */
    private String photo;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
