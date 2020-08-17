package com.weds.bean.base;

import com.weds.bean.locale.CustomMessageSource;
import com.weds.core.constants.CoreConstants;
import com.weds.core.resp.JsonResult;

import javax.annotation.Resource;

/**
 * 控制父类
 *
 * @author sxm
 */
public class BaseController extends com.weds.core.base.BaseController {

    @Resource
    private CustomMessageSource customMessageSource;

    @Override
    protected <T> JsonResult<T> message(String code, String msg, T data) {
        String message = customMessageSource.getMessage(msg);
        return JsonResult.get(code, message, data);
    }

    protected <T> JsonResult<T> message(String code, T data) {
        String message = customMessageSource.getMessage(code);
        return JsonResult.get(code, message, data);
    }

    protected <T> JsonResult<T> message(String code) {
        String message = customMessageSource.getMessage(code);
        return JsonResult.get(code, message, null);
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
