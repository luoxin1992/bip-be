/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

/**
 * 查询窗口简版Result
 * <p>
 * 查询全部
 * 查询绑定
 *
 * @author luoxin
 * @version 2017-5-14
 */
public class CounterQuerySimpleResult extends BaseResult {
    /**
     * ID
     */
    private Long id;
    /**
     * 编号
     */
    private String number;
    /**
     * 名称
     */
    private String name;

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
}
