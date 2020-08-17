package com.weds.core.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @explain 分页工具
 */
@ApiModel
public class BaseCommPager<T> implements Serializable {
    private static final long serialVersionUID = -224189960966026897L;

    @ApiModelProperty(value = "总条数")
    @JsonProperty("totalRecord")
    private long totalRecord;

    @ApiModelProperty(value = "总页数")
    @JsonProperty("totalPage")
    private int totalPage;

    @ApiModelProperty(value = "当前页码")
    @JsonProperty("pageIndex")
    private int pageIndex;

    @ApiModelProperty(value = "当前页码记录数")
    @JsonProperty("currenRecords")
    private int currenRecords;

    @ApiModelProperty(value = "每页容量")
    @JsonProperty("pageSize")
    private int pageSize;

    @ApiModelProperty(value = "数据记录")
    private List<T> records;

    public BaseCommPager() {
        this.records = new ArrayList<T>();
        this.totalRecord = -1; // 总条数
        this.pageSize = -1; // 每页容量
        this.totalPage = -1; // 总页数
        this.pageIndex = -1; // 当前页码
    }

    public BaseCommPager(List<T> pagerlist) {
        if (pagerlist instanceof Page) {
            Page<T> page = (Page<T>) pagerlist;
            this.records = page.getResult();
            this.currenRecords = page.size();
            this.pageSize = page.getPageSize();
            this.pageIndex = page.getPageNum();
            if (page.isCount()) {
                this.totalRecord = page.getTotal();
                this.totalPage = page.getPages();
            } else {
                this.totalRecord = -1;
                this.totalPage = -1;
            }
        }
    }

    public long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public int getCurrenRecords() {
        return currenRecords;
    }

    public void setCurrenRecords(int currenRecords) {
        this.currenRecords = currenRecords;
    }
}