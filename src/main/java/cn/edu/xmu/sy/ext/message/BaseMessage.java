/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.message;

/**
 * Message基类
 *
 * @author luoxin
 * @version 2017-4-25
 */
public class BaseMessage {
    /**
     * ID
     */
    private Long id;
    /**
     * 类型
     */
    private String type;

    public BaseMessage(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
