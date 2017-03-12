/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import java.util.List;

/**
 * 用户查询结果
 *
 * @author luoxin
 * @version 2017-3-11
 */
public class UserQueryResult {
    private Long id;
    private String number;
    private String name;
    private String photo;
    private List<FingerprintQueryResult> fingers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<FingerprintQueryResult> getFingers() {
        return fingers;
    }

    public void setFingers(List<FingerprintQueryResult> fingers) {
        this.fingers = fingers;
    }
}
