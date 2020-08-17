package com.weds.etl.entity;

import java.util.List;

public class SchedulerEntity {
    private String jobGroupName;
    private List<JobEntity> list;

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public List<JobEntity> getList() {
        return list;
    }

    public void setList(List<JobEntity> list) {
        this.list = list;
    }
}
