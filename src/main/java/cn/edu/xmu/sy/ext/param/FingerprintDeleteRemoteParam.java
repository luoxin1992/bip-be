/*
 * Copyright © 2017 Xiamen University. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.param;

import cn.com.lx1992.lib.base.param.BaseParam;

/**
 * 删除远程指纹Param
 *
 * @author luoxin
 * @version 2017-5-11
 */
public class FingerprintDeleteRemoteParam extends BaseParam {
    /**
     * UUID
     */
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
