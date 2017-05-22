/*
 * Copyright © 2017 LuoXin. All Rights Reserved.
 */
package cn.edu.xmu.sy.ext.exception;

import cn.com.lx1992.lib.base.exception.BaseException;
import cn.com.lx1992.lib.base.meta.IResultEnum;

/**
 * 封装业务逻辑中发生的异常
 *
 * @author luoxin
 * @version 1.0
 * @created 2017-3-18
 */
public class BizException extends BaseException {
    public BizException(IResultEnum result) {
        super(result);
    }

    public BizException(IResultEnum result, Object... args) {
        super(result, args);
    }
}
