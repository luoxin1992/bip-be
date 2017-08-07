/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.result;

import cn.com.lx1992.lib.base.result.BaseResult;

import java.util.List;

/**
 * 指纹手指名称Result
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintListFingerResult extends BaseResult {
    /**
     * 手指名称
     */
    private List<String> fingers;

    public List<String> getFingers() {
        return fingers;
    }

    public void setFingers(List<String> fingers) {
        this.fingers = fingers;
    }
}
