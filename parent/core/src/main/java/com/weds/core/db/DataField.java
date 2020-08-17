package com.weds.core.db;

import java.io.Serializable;

/**
 * <pre>
 * <dt><b>类名：</b></dt>
 * <dd>DataField</dd>
 * <dt><b>描述：</b></dt>
 * <dd>数据集字段信息</dd>
 * <dt><b>日期：</b></dt>
 * <dd>2016-5-3 上午11:25:25</dd>
 * </pre>
 *
 * @author SXM
 * @version 0.1
 */
public class DataField implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * SQL类型
     */
    private String sqlType;

    /**
     * Java类名称
     */
    private String className;

    /**
     * 显示长度
     */
    private int displaySize;

    /**
     * 小数位数
     */
    private int scale;

    /**
     * 列数据类型
     */
    private int type;

    /**
     * 获取字段名称
     *
     * @return 字段名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置字段名称
     *
     * @param name 字段名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取字段中文别名
     *
     * @return 字段中文别名
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 设置字段中文别名
     *
     * @param alias 字段中文别名
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * 获取字段sql类型
     *
     * @return 字段sql类型
     */
    public String getSqlType() {
        return sqlType;
    }

    /**
     * 设置字段sql类型
     *
     * @param sqlType 字段sql类型
     */
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    /**
     * 获取字段java类型
     *
     * @return 字段java类型
     */
    public String getClassName() {
        return className;
    }

    /**
     * 设置字段java类型
     *
     * @param className 字段java类型
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * 获取显示长度
     *
     * @return 显示长度
     */
    public int getDisplaySize() {
        return displaySize;
    }

    /**
     * 设置显示长度
     *
     * @param displaySize 显示长度
     */
    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>getColumnTypeByClassName</dd>
     * <dt><b>描述：</b></dt>
     * <dd>根据JAVA数据类型获取字段类型</dd>
     * </pre>
     *
     * @param className
     * @return
     */
    public static int getColumnTypeByClassName(String className) {
        int type = 1;
        if (className.equals("java.lang.String")
                || className.equals("java.sql.Clob"))
            type = 1;
        else if (className.equals("java.lang.Integer")
                || className.equals("java.lang.Byte")
                || className.equals("java.lang.Short"))
            type = 2;
        else if (className.equals("java.lang.Double")
                || className.equals("java.lang.Float"))
            type = 3;
        else if (className.equals("java.lang.Long")
                || className.equals("java.math.BigInteger"))
            type = 4;
        else if (className.equals("java.math.BigDecimal"))
            type = 5;
        else if (className.equals("java.sql.Date"))
            type = 6;
        else if (className.equals("java.sql.Time"))
            type = 7;
        else if (className.equals("java.sql.Timestamp"))
            type = 8;
        else if (className.equals("[B") || className.equals("java.sql.Blob")
                || className.equals("oracle.sql.BLOB"))
            type = 9;
        return type;
    }

    /**
     * 获取小数位数
     *
     * @return 小数位数
     */
    public int getScale() {
        return scale;
    }

    /**
     * 设置小数位数
     *
     * @param scale 小数位数
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * 转化为字符串
     *
     * @return 字符串
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String tmpStr = "name=" + this.name + ",alias=" + this.alias
                + ",sqlType=" + this.sqlType + ",className=" + this.className
                + ",displaySize=" + this.displaySize + ",scale=" + this.scale;
        return tmpStr;
    }

    /**
     * 克隆
     *
     * @return Object 对象
     * @throws CloneNotSupportedException
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        DataField obj = (DataField) super.clone();
        obj.name = name;
        obj.alias = alias;
        obj.sqlType = sqlType;
        obj.className = className;
        obj.displaySize = displaySize;
        obj.scale = scale;
        return obj;
    }
}
