package com.weds.rpt.entity;

import java.util.List;

public class TabInfo {

    private transient String rptId;

    // private transient List<GroupInfo> groupList;

    private String title;

    private String querySql;

    private List<ColInfo> columns;

    private List<QueryInfo> query;

    public String getRptId() {
        return rptId;
    }

    public void setRptId(String rptId) {
        this.rptId = rptId;
    }

    // public List<GroupInfo> getGroupList() {
    //     return groupList;
    // }
    //
    // public void setGroupList(List<GroupInfo> groupList) {
    //     this.groupList = groupList;
    // }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public List<ColInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColInfo> columns) {
        this.columns = columns;
    }

    public List<QueryInfo> getQuery() {
        return query;
    }

    public void setQuery(List<QueryInfo> query) {
        this.query = query;
    }
}
