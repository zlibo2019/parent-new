package com.weds.rpt.entity;

import java.util.List;

public class GroupInfo {
    private String label;
    private String align;
    private List<ColInfo> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public List<ColInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ColInfo> children) {
        this.children = children;
    }
}
