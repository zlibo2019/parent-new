package com.weds.rpt.entity;

public class ColInfo {
    private int index;

    private String prop;

    private String label;

    private String width;

    private String align;

    private String fixed;

    private String style;

    private String type;

    private String format;

    private String group;

    private boolean span;

    private boolean hidden;

    private boolean select;

    private boolean sumType;

    private boolean queryFlag;

    private String queryType;

    private String queryOpt;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public boolean isSumType() {
        return sumType;
    }

    public void setSumType(boolean sumType) {
        this.sumType = sumType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSpan() {
        return span;
    }

    public void setSpan(boolean span) {
        this.span = span;
    }

    public boolean isQueryFlag() {
        return queryFlag;
    }

    public void setQueryFlag(boolean queryFlag) {
        this.queryFlag = queryFlag;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryOpt() {
        return queryOpt;
    }

    public void setQueryOpt(String queryOpt) {
        this.queryOpt = queryOpt;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
