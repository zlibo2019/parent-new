package com.weds.core.license;

/**
 * 注册码信息
 *
 * @author SXM
 * @version 0.1
 */
public class LicenseEntity {
    // 产品名称
    private String product;
    // 客户名称
    private String customer;
    // 开始时间
    private long startDate;
    // 结束时间
    private long endDate;
    // 机器码
    private String[] id;
    // 注册信息
    private InfoEntity info;

    private String[] temp;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String[] getId() {
        return id;
    }

    public void setId(String[] id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public InfoEntity getInfo() {
        return info;
    }

    public void setInfo(InfoEntity info) {
        this.info = info;
    }

    public String[] getTemp() {
        return temp;
    }

    public void setTemp(String[] temp) {
        this.temp = temp;
    }
}
