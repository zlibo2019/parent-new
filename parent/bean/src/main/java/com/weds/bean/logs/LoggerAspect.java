package com.weds.bean.logs;

import com.google.gson.Gson;
import com.weds.bean.jwt.JwtUtils;
import com.weds.core.annotation.Logs;
import com.weds.core.utils.AspectUtils;
import com.weds.core.utils.DateUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class LoggerAspect {
    private Logger log = LogManager.getLogger();

    @Resource
    private HttpServletRequest request;

    @Resource
    private LogWriteServer logWriteServer;

    @Resource
    private LogParams logParams;

    @Pointcut("execution(* com.weds..*(..)) && @annotation(com.weds.core.annotation.Logs)")
    public void userPointCut() {
    }

    //&& !execution(* com.weds..*.mapper..*(..))
    @Pointcut("execution(* com.weds..*(..))")
    public void sysPointCut() {
    }

    @Around("userPointCut()")
    public Object userLogWrite(ProceedingJoinPoint point) {
        Object obj = null;
        boolean flag = false;
        String errMsg = "";
        Gson gson = new Gson();
        long startTime = System.currentTimeMillis();

        try {
            obj = point.proceed();
            flag = true;
        } catch (Throwable e) {
            log.error(ExceptionUtils.getStackTrace(e));
            errMsg = e.toString();
        }

        if (!logParams.isActive()) {
            return obj;
        }

        long offTime = System.currentTimeMillis() - startTime;

        Method method = AspectUtils.findMethodByPoint(point);
        if (method == null) {
            return obj;
        }

        boolean hasAnnotation = method.isAnnotationPresent(Logs.class);
        if (hasAnnotation) {
            Logs logInfo = method.getAnnotation(Logs.class);
            boolean valid = logInfo.valid();
            if (!valid) {
                return obj;
            }

            String methodDesc = logInfo.value();
            if (method.isAnnotationPresent(ApiOperation.class) && "".equals(methodDesc))
                methodDesc = method.getAnnotation(ApiOperation.class).value();
            String logPosition = logInfo.logsPostion().toString();
            Class<?> clazz = point.getTarget().getClass();
            Object[] params = point.getArgs();
            String strArgs = "";
            strArgs = getArgs(params, strArgs);

            String argsvalue = "";
            if (Logs.Position.Controller.toString().equals(logPosition)) {
                Enumeration<?> paraNames = request.getParameterNames();
                Map<String, Object> map = new HashMap<String, Object>();
                while (paraNames.hasMoreElements()) {
                    String thisName = paraNames.nextElement().toString();
                    String thisValue = request.getParameter(thisName);
                    map.put(thisName, thisValue);
                }
                argsvalue = getArgsValue(gson, argsvalue, map, point.getArgs());
            }

            LogEntity logEntity = new LogEntity();
            logEntity.setClazzName(clazz.getName());
            logEntity.setPosition(logPosition);
            logEntity.setUrl(request.getRequestURI());
            logEntity.setMethodName(method.getName());
            logEntity.setMethodDesc(methodDesc);
            logEntity.setArgsName(strArgs);
            logEntity.setArgsValue(argsvalue.replaceAll("'", "''"));
            logEntity.setJsonResult(gson.toJson(obj));
            logEntity.setOffTime(String.valueOf(offTime));
            logEntity.setCurrDate(DateUtils.getCurrentDateTime());
            logEntity.setIp(request.getRemoteAddr());
            logEntity.setUser(JwtUtils.getJwtData(request).getString("userSerial"));
            logEntity.setMethod(request.getMethod());
            logEntity.setErrMessage(errMsg);

            if (flag) {
                logWriteServer.handleInfoLogs(logEntity);
            } else {
                logWriteServer.handleErrorLogs(logEntity);
            }
        }
        return obj;
    }

    private String getArgsValue(Gson gson, String argsvalue, Map<String, Object> map, Object[] args2) {
        if (map.keySet().size() > 0) {
            argsvalue = gson.toJson(map);
        } else {
            Object[] args = args2;
            if (args != null) {
                argsvalue = gson.toJson(args);
            }
        }
        return argsvalue;
    }

    private String getArgs(Object[] params, String strArgs) {
        StringBuilder strArgsBuilder = new StringBuilder(strArgs);
        for (int j = 0; j < params.length; j++) {
            String name = null;
            if (params[j] != null) {
                name = params[j].getClass().getName();
            }
            if (j == params.length - 1) {
                strArgsBuilder.append(name);
            } else {
                strArgsBuilder.append(name).append("|");
            }
        }
        strArgs = strArgsBuilder.toString();
        return strArgs;
    }

    @AfterThrowing(value = "sysPointCut()", throwing = "e")
    public void throwLogWrite(JoinPoint point, Exception e) {
        Method method = AspectUtils.findMethodByPoint(point);
        String logPosition = "";
        String methodDesc = "";
        if (method == null) {
            return;
        }

        Gson gson = new Gson();
        Class<?> clazz = point.getTarget().getClass();
        boolean hasAnnotation = method.isAnnotationPresent(Logs.class);
        if (hasAnnotation) {
            Logs logInfo = method.getAnnotation(Logs.class);
            methodDesc = logInfo.value();
            if (method.isAnnotationPresent(ApiOperation.class) && "".equals(methodDesc))
                methodDesc = method.getAnnotation(ApiOperation.class).value();
            logPosition = logInfo.logsPostion().toString();
        }
        Object[] obj = point.getArgs();
        String strArgs = "";
        strArgs = getArgs(obj, strArgs);

        String argsvalue = "";
        if (hasAnnotation && Logs.Position.Controller.toString().equals(logPosition)) {
            Enumeration<?> paraNames = request.getParameterNames();
            Map<String, Object> map = new HashMap<String, Object>();
            for (; paraNames.hasMoreElements(); ) {
                String thisName = paraNames.nextElement().toString();
                String thisValue = request.getParameter(thisName);
                map.put(thisName, thisValue);
            }
            argsvalue = getArgsValue(gson, argsvalue, map, point.getArgs());
        }

        LogEntity logEntity = new LogEntity();
        logEntity.setClazzName(clazz.getName());
        logEntity.setPosition(logPosition);
        logEntity.setUrl(request.getRequestURI());
        logEntity.setMethodName(method.getName());
        logEntity.setMethodDesc(methodDesc);
        logEntity.setArgsName(strArgs);
        logEntity.setArgsValue(argsvalue.replaceAll("'", "''"));
        logEntity.setOffTime(null);
        logEntity.setCurrDate(DateUtils.getCurrentDateTime());
        logEntity.setIp(request.getRemoteAddr());
        logEntity.setUser(JwtUtils.getJwtData(request).getString("userSerial"));
        logEntity.setMethod(request.getMethod());
        logEntity.setErrMessage(e.toString());
        logWriteServer.handleErrorLogs(logEntity);
        log.error(ExceptionUtils.getStackTrace(e));
    }

    @AfterReturning(value = "sysPointCut()")
    public void succLogWrite(JoinPoint point) {

    }
}
