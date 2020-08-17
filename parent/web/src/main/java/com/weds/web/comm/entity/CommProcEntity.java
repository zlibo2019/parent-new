package com.weds.web.comm.entity;

import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * 入参实体类：CommProcReq 对应接口：ICommProcWebService
 */
@ApiModel
public class CommProcEntity {
    // 数据源标识（存储过程名称）
    private String procName;
    // 参数集合
    private List<Object> params;
    // 返回结果集个数
    private Integer outNum;

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public Integer getOutNum() {
        return outNum;
    }

    public void setOutNum(Integer outNum) {
        this.outNum = outNum;
    }

    public CommProcEntity() {
    }

    public CommProcEntity(String procName, List<Object> params, Integer outNum) {
        this.procName = procName;
        this.params = params;
        this.outNum = outNum;
    }
}
