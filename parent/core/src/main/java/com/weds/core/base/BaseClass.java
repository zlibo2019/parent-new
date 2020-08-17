package com.weds.core.base;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.weds.core.constants.CoreConstants;
import com.weds.core.resp.JsonResult;

import java.lang.reflect.Type;

/**
 * 父类
 */
public class BaseClass {

    /**
     * 将对象转化为Json字符串
     *
     * @param obj 转化对象
     * @return
     */
    protected String toJson(Object obj) {
        return this.toJson(obj, false, false, false);
    }

    protected String toJson(Object obj, boolean nullFlag, boolean htmlFlag, boolean longFlag) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (nullFlag) {
            gsonBuilder.serializeNulls();
        }

        if (htmlFlag) {
            gsonBuilder.disableHtmlEscaping();
        }

        if (longFlag) {
            gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
        }
        return gsonBuilder.create().toJson(obj);
    }

    /**
     * 获取Gson对象
     *
     * @return
     */
    protected Gson getGson() {
        Gson gson = new GsonBuilder().create();
        return gson;
    }

    /**
     * @param obj
     * @return
     */
    protected String toJsonLcwu(Object obj) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.toJson(obj);
    }

    /**
     * @param obj
     * @return
     */
    protected String toJsonIdentity(Object obj) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
        return gson.toJson(obj);
    }

    /**
     * 将Json字符串转化为对象
     *
     * @param json Json字符串
     * @return
     */
    protected <T> T fromJson(String json, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, type);
    }

    /**
     * @param code
     * @param msg
     * @param data
     * @param <T>
     * @return
     */
    protected <T> JsonResult<T> message(String code, String msg, T data) {
        return JsonResult.get(code, msg, data);
    }

    /**
     * @param code
     * @param msg
     * @param <T>
     * @return
     */
    protected <T> JsonResult<T> message(String code, String msg) {
        return JsonResult.get(code, msg, null);
    }

    /**
     * @param code
     * @param <T>
     * @return
     */
    protected <T> JsonResult<T> message(String code, T data) {
        return JsonResult.get(code, null, data);
    }

    /**
     * @param code
     * @param <T>
     * @return
     */
    protected <T> JsonResult<T> message(String code) {
        return JsonResult.get(code, null, null);
    }

    /**
     * 操作成功
     *
     * @param msg
     * @param data
     * @return
     */
    protected <T> JsonResult<T> succMsg(String msg, T data) {
        return message(CoreConstants.SUCCESSED_FLAG, msg, data);
    }

    /**
     * 操作成功
     *
     * @param msg
     * @return
     */
    protected <T> JsonResult<T> succMsg(String msg) {
        return succMsg(msg, null);
    }

    /**
     * 操作成功
     *
     * @param data
     * @return
     */
    @Deprecated
    protected <T> JsonResult<T> succMsgData(T data) {
        return succMsg(CoreConstants.SUCCESSED_MSG, data);
    }

    /**
     * 操作成功
     *
     * @return
     */
    @Deprecated
    protected <T> JsonResult<T> succMsg() {
        return succMsgData(null);
    }

    /**
     * 操作失败
     *
     * @param message
     * @param data
     * @return
     */
    protected <T> JsonResult<T> failMsg(String message, T data) {
        return message(CoreConstants.FAILED_FLAG, message, data);
    }

    /**
     * 操作失败
     *
     * @param message
     * @return
     */
    protected <T> JsonResult<T> failMsg(String message) {
        return failMsg(message, null);
    }

    /**
     * 操作失败
     *
     * @param data
     * @param <T>
     * @return
     */
    @Deprecated
    protected <T> JsonResult<T> failMsgData(T data) {
        return failMsg(CoreConstants.FAILED_MSG, data);
    }

    /**
     * 操作失败
     *
     * @return
     */
    @Deprecated
    protected <T> JsonResult<T> failMsg() {
        return failMsgData(null);
    }
}
