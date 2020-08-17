package com.weds.core.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class BasePageSearch<T> {
    private static final long serialVersionUID = -2816565786374080978L;

    @ApiModelProperty(required = false, value = "是否分页,默认true", example = "true", dataType = "bool")
    @JsonProperty("pageFlag")
    private boolean pageFlag = true;

    @ApiModelProperty(required = false, value = "页码,默认1", example = "1", dataType = "int")
    @JsonProperty("pageIndex")
    private Integer pageIndex;

    @ApiModelProperty(required = false, value = "每页条数,默认20", example = "20", dataType = "int")
    @JsonProperty("pageSize")
    private Integer pageSize;

    @ApiModelProperty(required = false, value = "是否查询数据总条数,默认false", example = "true", dataType = "bool")
    private boolean count = true;

    @ApiModelProperty(required = false, value = "排序字段  ", example = "XXX desc,XXX asc", dataType = "string")
    @JsonProperty("orderBy")
    private String orderBy;

    @ApiModelProperty(required = false, value = "查询条件主体")
    @NotNull
    private T search;

    public boolean isPageFlag() {
        return pageFlag;
    }

    public void setPageFlag(boolean pageFlag) {
        this.pageFlag = pageFlag;
    }

    /**
     * 页码 从1开始
     *
     * @return
     */
    public Integer getPageIndex() {
        if (pageIndex == null || pageIndex <= 0) {
            return 1;
        }
        return pageIndex;
    }

    /**
     * 页码 从1开始
     *
     * @param pageIndex
     */
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * 每页条数
     *
     * @return
     */
    public Integer getPageSize() {
        if (pageSize == null || pageSize <= 0) {
            return 20;
            // return ConfigBase.getDefaultPageSize();
        }
        return pageSize;
    }

    /**
     * 每页条数
     *
     * @param pageSize
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 是否查询数据总条数,默认false
     *
     * @return
     */
    public boolean isCount() {
        return count;
    }

    /**
     * 是否查询数据总条数,默认false
     *
     * @param count
     */
    public void setCount(boolean count) {
        this.count = count;
    }

    /**
     * 排序字段
     *
     * @return
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 排序字段
     *
     * @param orderBy
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public T getSearch() {
        return search;
    }

    public void setSearch(T search) {
        this.search = search;
    }
}
