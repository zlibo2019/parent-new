package com.weds.bean.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import com.weds.bean.base.BaseController;
import com.weds.bean.logs.LogEntity;
import com.weds.bean.logs.LogWriteServer;
import com.weds.bean.utils.SpringContextHolder;
import com.weds.core.constants.CoreConstants;
import com.weds.core.resp.JsonResult;
import com.weds.core.utils.DateUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * RESTFUL API的统一异常拦截处理
 */
@ControllerAdvice
public class RestExceptionHandler extends BaseController {
    private Logger log = LogManager.getLogger();

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult handleException(HttpServletRequest req, Exception ex) throws Exception {
        ex.printStackTrace();

        Gson gson = new Gson();
        String argsvalue = "";
        Enumeration<?> paraNames = req.getParameterNames();
        Map<String, Object> map = new HashMap<String, Object>();
        for (Enumeration<?> e1 = paraNames; e1.hasMoreElements(); ) {
            String thisName = e1.nextElement().toString();
            String thisValue = req.getParameter(thisName);
            map.put(thisName, thisValue);
        }

        if (map.keySet().size() > 0) {
            argsvalue = gson.toJson(map);
        }

        LogWriteServer logWriteServer = SpringContextHolder.getBean("logWriteServer");

        LogEntity logEntity = new LogEntity();
        logEntity.setClazzName(this.getClass().getName());
        logEntity.setMethodName("handleException");
        logEntity.setArgsValue(argsvalue);
        logEntity.setCurrDate(DateUtils.getCurrentDateTime());
        logEntity.setErrMessage(ex.toString());
        logWriteServer.handleErrorLogs(logEntity);
        log.error(ExceptionUtils.getStackTrace(ex));

        // 创建返回类
        // 处理错误
        if (ex instanceof HttpMessageNotReadableException) {
            return message(CoreConstants.PARA_ERROR_FLAG);
        } else if (ex instanceof MethodArgumentNotValidException) { // Valid失败
            // 组织错误信息
            Map<String, String> errors = new HashMap<String, String>();
            MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
            int i = 1;
            for (ObjectError error : exception.getBindingResult().getAllErrors()) {
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                } else {
                    errors.put("other_error" + i, error.getDefaultMessage());
                }
            }
            return message(CoreConstants.PARA_ERROR_FLAG, errors);
        } else if (ex instanceof TransactionalRuntimeException
                || ex instanceof InvalidFormatException) {
            return message(CoreConstants.PARA_ERROR_FLAG);
        } else if (ex instanceof SignException) { // 签名错误
            return message(CoreConstants.SIGN_ERROR_FLAG);
        } else if (ex instanceof EncodeException) { // 编码解码错误
            return message(CoreConstants.ENCODE_ERROR_FLAG);
        } else { // 系统错误
            return message(CoreConstants.SYSTEM_ERROR_FLAG);
        }
    }
}