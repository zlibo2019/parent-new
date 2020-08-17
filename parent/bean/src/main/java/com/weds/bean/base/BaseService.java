package com.weds.bean.base;

import com.weds.bean.locale.CustomMessageSource;
import com.weds.core.constants.CoreConstants;
import com.weds.core.resp.JsonResult;

import javax.annotation.Resource;

/**
 * 服务父类
 *
 * @author sxm
 */
public class BaseService extends com.weds.core.base.BaseService {
    @Resource
    private CustomMessageSource customMessageSource;

    @Override
    protected <T> JsonResult<T> message(String code, String msg, T data) {
        String message = customMessageSource.getMessage(msg);
        return JsonResult.get(code, message, data);
    }

    @Override
    protected <T> JsonResult<T> succMsgData(T data) {
        return succMsg(CoreConstants.SUCCESSED_FLAG, data);
    }

    @Override
    protected <T> JsonResult<T> succMsg() {
        return succMsgData(null);
    }

    @Override
    protected <T> JsonResult<T> failMsgData(T data) {
        return failMsg(CoreConstants.FAILED_FLAG, data);
    }

    @Override
    protected <T> JsonResult<T> failMsg() {
        return failMsgData(null);
    }
}
