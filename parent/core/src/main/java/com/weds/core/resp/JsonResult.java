package com.weds.core.resp;

import com.weds.core.base.BaseClass;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 默认code为成功
 */
@ApiModel
public class JsonResult<T> extends BaseClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态码", notes = "600是成功,其他都是失败")
    private String code;

    @ApiModelProperty(value = "状态码描述信息", notes = "例如失败，参数错误")
    private String msg;

    @ApiModelProperty(value = "具体的业务数据", notes = "")
    private T data;

    public JsonResult(String code, String msg, T data) {
        setCode(code);
        setMsg(msg);
        setData(data);
    }

    public JsonResult(String code, String msg) {
        setCode(code);
        setMsg(msg);
    }

    public JsonResult(T data) {
        this.data = data;
    }

    public JsonResult() {
    }

    @Override
    public String toString() {
        return toJson(new JsonResult<>(this.code, this.msg, this.data));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> JsonResult<T> get(String code, String msg, T data) {
        return new JsonResult<>(code, msg, data);
    }

    public static <T> JsonResult<T> get(String code, String msg) {
        return JsonResult.get(code, msg, null);
    }
}
