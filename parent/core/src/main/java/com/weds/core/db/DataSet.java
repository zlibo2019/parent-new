package com.weds.core.db;

import java.io.PrintStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * <pre>
 * <dt><b>类名：</b></dt>
 * <dd>DataSet</dd>
 * <dt><b>描述：</b></dt>
 * <dd>数据集信息</dd>
 * <dt><b>日期：</b></dt>
 * <dd>2016-5-3 上午11:26:05</dd>
 * </pre>
 *
 * @author SXM
 * @version 0.1
 */
public class DataSet implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    private static byte space = " ".getBytes()[0]; // space

    private ArrayList<ArrayList<Object>> alData = new ArrayList<ArrayList<Object>>(); // 存放数据的ArrayListst
    private ArrayList<DataField> alField = new ArrayList<>(); // 存放数据字段信息ArrayList，有顺序
    private HashMap<String, String> htNameNum = new HashMap<>(); // 列名和列号的映射

    private int colCount = 0; // 列数目
    private int tmprowIndex = -1; // 暂无用处
    private int columnType[] = new int[0]; // 列类型（整型）
    private String fileEncoding;

    /**
     * 构造函数
     *
     * @param dataField
     * @param fileEncoding
     */
    public DataSet(DataField[] dataField, String fileEncoding) {
        this.fileEncoding = fileEncoding;
        setDataField(dataField);
    }

    /**
     * 构造函数
     *
     * @param dataField DataField数组
     */
    public DataSet(DataField[] dataField) {
        setDataField(dataField);
    }

    /**
     * 构造函数
     */
    public DataSet() {

    }

    /**
     * 设置数据格式，若实例化时使用方法DataSet()时，必须调用该方法。
     *
     * @param dataField DataField数组
     */
    public void setDataField(DataField[] dataField) {
        for (int i = 0; i < dataField.length; i++) {
            this.addField(dataField[i]);
        }
    }

    /**
     * 增加字段
     *
     * @param dataField DataField
     * @return 所增加列所在的位置
     */
    public int addField(DataField dataField) {
        int s = alField.size();
        String colName = dataField.getName().toUpperCase();
        this.htNameNum.put(colName, String.valueOf(s));
        alField.add(dataField);
        this.colCount = s + 1;

        int tmpType[] = this.columnType.clone();
        this.columnType = new int[s + 1];
        System.arraycopy(tmpType, 0, this.columnType, 0, s);

        this.columnType[s] = 0; // 初始值
        switch (dataField.getClassName()) {
            case "java.lang.String":
                this.columnType[s] = 1;
                break;
            case "java.lang.Integer":
                this.columnType[s] = 2;
                break;
            case "java.lang.Double":
                this.columnType[s] = 3;
                break;
            case "java.lang.Float":
                this.columnType[s] = 4;
                break;
            case "java.lang.Long":
                this.columnType[s] = 5;
                break;
            case "java.lang.Byte":
                this.columnType[s] = 6;
                break;
            case "java.lang.Short":
                this.columnType[s] = 7;
                break;
            case "java.math.BigInteger":
                this.columnType[s] = 8;
                break;
            case "java.math.BigDecimal":
                this.columnType[s] = 9;
                break;
            case "java.sql.Blob":
            case "oracle.sql.BLOB":
                this.columnType[s] = 10;
                break;
        }
        return s;
    }

    /**
     * 移除字段
     *
     * @param colIndex 列序号
     */
    public void removeField(int colIndex) {
        this.alField.remove(colIndex);
        for (int i = 0; i < this.alData.size(); i++) {
            this.getRow(i).remove(colIndex);
        }
        this.colCount = this.colCount - 1;
    }

    /**
     * 获取所有字段
     *
     * @return 行数
     */
    public DataField[] getAllField() {
        DataField dataField[] = new DataField[this.alField.size()];
        for (int i = 0; i < dataField.length; i++) {
            dataField[i] = alField.get(i);
        }
        return dataField;
    }

    /**
     * 获取DataSet的行数。
     *
     * @return 行数
     */
    public int size() {
        return alData.size();
    }

    /**
     * 获取列数量
     *
     * @return 整数
     */
    public int getColumnCount() {
        return colCount;
    }

    /**
     * 获取字段
     *
     * @param colIndex 列号(从0开始排)
     * @return DataField
     * @throws Exception
     * @throws Exception
     */
    public DataField getDataField(int colIndex) throws Exception {
        if (colIndex >= this.alField.size()) {
            throw new Exception("列号错误，应该小于: " + this.alField.size());
        }
        return this.alField.get(colIndex);
    }

    /**
     * 获取列名
     *
     * @param colIndex 列号(从0开始排)
     * @return 字符串型的列名称
     * @throws Exception
     * @throws Exception
     */
    public String getColumnName(int colIndex) throws Exception {
        return this.getDataField(colIndex).getName();
    }

    /**
     * 获取列别名
     *
     * @param colIndex 列号(从0开始排)
     * @return 字符串型的列别名
     * @throws Exception
     * @throws Exception
     */
    public String getColumnAlias(int colIndex) throws Exception {
        return this.getDataField(colIndex).getAlias();
    }

    /**
     * 获取列sql类型
     *
     * @param colIndex 列号(从0开始排)
     * @return 字符串型的sql类型
     * @throws Exception
     * @throws Exception
     */
    public String getSqlType(int colIndex) throws Exception {
        return this.getDataField(colIndex).getSqlType();
    }

    /**
     * 获取列java类名称
     *
     * @param colIndex 列号(从0开始排)
     * @return 字符串型的java类名称
     * @throws Exception
     * @throws Exception
     */
    public String getClassName(int colIndex) throws Exception {
        return this.getDataField(colIndex).getClassName();
    }

    /**
     * 获取列的显示长度
     *
     * @param colIndex 序号
     * @return 整数
     * @throws Exception
     * @throws Exception
     */
    public int getColumnDisplaySize(int colIndex) throws Exception {
        return this.getDataField(colIndex).getDisplaySize();
    }

    /**
     * 获取列的小数位数
     *
     * @param colIndex 列序号
     * @return 整数
     * @throws Exception
     * @throws Exception
     */
    public int getColumnScale(int colIndex) throws Exception {
        return this.getDataField(colIndex).getScale();
    }

    /**
     * 获取列序号
     *
     * @param colName 列名称
     * @return int ，列号
     * @throws Exception
     * @throws Exception
     */
    public int getColumnNo(String colName) throws Exception {
        checkColName(colName);
        colName = colName.toUpperCase();
        String tmpI = htNameNum.get(colName);
        return Integer.parseInt(tmpI);
    }

    /**
     * 获取列名
     *
     * @param colName 列名称
     * @return 字符串型的列名称
     * @throws Exception
     * @throws Exception
     */
    public String getColumnName(String colName) throws Exception {
        return this.getColumnName(getColumnNo(colName));
    }

    /**
     * 获取列别名
     *
     * @param colName 列名称
     * @return 字符串型的列别名
     * @throws Exception
     * @throws Exception
     */
    public String getColumnAlias(String colName) throws Exception {
        return this.getColumnAlias(getColumnNo(colName));
    }

    /**
     * 获取列sql类型
     *
     * @param colName 列名称
     * @return 字符串型的sql列类型
     * @throws Exception
     * @throws Exception
     */
    public String getSqlType(String colName) throws Exception {
        return this.getSqlType(getColumnNo(colName));
    }

    /**
     * 获取列java类型名称
     *
     * @param colName 列名称
     * @return 字符串型的java类型名称
     * @throws Exception
     * @throws Exception
     */
    public String getClassName(String colName) throws Exception {
        return this.getClassName(getColumnNo(colName));
    }

    /**
     * 获取列显示长度
     *
     * @param colName 列名称
     * @return int 列显示长度
     * @throws Exception
     * @throws Exception
     */
    public int getColumnDisplaySize(String colName) throws Exception {
        return this.getColumnDisplaySize(getColumnNo(colName));
    }

    /**
     * 获取列的小数位数
     *
     * @param colName 列名称
     * @return 整型
     * @throws Exception
     * @throws Exception
     */
    public int getColumnScale(String colName) throws Exception {
        return this.getColumnScale(getColumnNo(colName));
    }

    /**
     * 获取字段
     *
     * @param colName 列名称
     * @return DataField
     * @throws Exception
     * @throws Exception
     */
    public DataField getDataField(String colName) throws Exception {
        return this.getDataField(getColumnNo(colName));
    }

    /**
     * 获取Object型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colIndex 列号(从0开始排)
     * @return Object
     * @throws Exception
     * @throws Exception
     */
    public Object getObject(int rowIndex, int colIndex) throws Exception {
        if (colIndex >= colCount) {
            throw new Exception("列号错误，应该小于: " + String.valueOf(colCount));
        }
        // 字符转换
        Object obj = this.getRow(rowIndex).get(colIndex);

        switch (this.columnType[colIndex]) {
            // case 0: // String
            // if (obj == null)
            // return "";
            // return new String((byte[]) obj);
            // case 1: // String
            // if (obj == null)
            // return "";
            // return new String((byte[]) obj);
            case 2: // Integer
                if (obj == null) {
                    return new Integer("0");
                }
                return obj;
            case 3: // Double
                if (obj == null) {
                    return new Double("0");
                }
                return obj;
            case 4: // Float
                if (obj == null) {
                    return new Float("0");
                }
                return obj;
            case 5: // Long
                if (obj == null) {
                    return new Long("0");
                }
                return new Long(new String((byte[]) obj));
            case 6: // Byte
                if (obj == null) {
                    return new Byte("0");
                }
                return new Byte(new String((byte[]) obj));
            case 7: // Short
                if (obj == null) {
                    return new Short("0");
                }
                return new Short(new String((byte[]) obj));
            case 8: // BigInteger
                if (obj == null) {
                    return new BigInteger("0");
                }
                return new BigInteger(new String((byte[]) obj));
            case 9: // BigDecimal
                if (obj == null) {
                    return new BigDecimal("0");
                }
                return new BigDecimal(new String((byte[]) obj));
            case 10: // BLOB
                if (obj == null) {
                    return "";
                }
                return obj;
            default:
                if (obj == null) {
                    return "";
                }
                return new String((byte[]) obj);

        }
    }

    /**
     * 获取String型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colIndex 列号(从0开始排)
     * @return String
     * @throws Exception
     * @throws Exception
     */
    public String getString(int rowIndex, int colIndex) throws Exception {
        if (colIndex >= colCount)
            throw new Exception("列号错误，应该小于: " + String.valueOf(colCount));

        Object obj = this.getRow(rowIndex).get(colIndex);
        switch (this.columnType[colIndex]) {
            // case 0: //String
            // return new String( (byte[]) obj );
            // case 1: //String
            // return new String( (byte[]) obj );
            case 2: // Integer
                return obj.toString();
            case 3: // Double
                if (obj == null) {
                    return "0";
                }
                return new BigDecimal((Double) (obj)).toString().trim();
            case 4: // Float
                if (obj == null) {
                    return "0";
                }
                return new BigDecimal((Float) (obj)).toString().trim();
            case 5: // Long
                if (obj == null) {
                    return "0";
                }
                return new BigDecimal((Long) (obj)).toString().trim();
            // case 6: // Byte
            // return new String((byte[]) obj);
            // case 7: // Short
            // return new String((byte[]) obj);
            // case 8: // BigInteger
            // return new String((byte[]) obj);
            // case 9: // BigDecimal
            // return new String((byte[]) obj);
            default:
                if (obj == null) {
                    return "";
                }
                return rightTrim((byte[]) obj);
        }
    }

    /**
     * right trim
     *
     * @param data byte[]
     * @return String
     */
    private String rightTrim(byte[] data) {
        int i = data.length - 1;
        for (; i >= 0; i--) {
            if (data[i] != space) {
                break;
            }
        }

        String retstr = "";
        if (null != fileEncoding && !"".equals(fileEncoding)) {
            try {
                retstr = new String(data, 0, i + 1, fileEncoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            retstr = new String(data, 0, i + 1);
        }
        // 字符转换
        return retstr;
    }

    /**
     * 获取int型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colIndex 列号(从0开始排)
     * @return int
     * @throws Exception
     * @throws Exception
     */
    public int getInt(int rowIndex, int colIndex) throws Exception {
        String tmp = this.getString(rowIndex, colIndex);
        if (tmp.equals("")) {
            tmp = "0";
        }
        return Integer.parseInt(tmp);
    }

    /**
     * 获取double型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colIndex 列号(从0开始排)
     * @return double
     * @throws Exception
     * @throws Exception
     */
    public double getDouble(int rowIndex, int colIndex) throws Exception {
        if (this.columnType[colIndex] == 3) {
            return (Double) (this.getObject(rowIndex, colIndex));
        } else {
            String tmp = this.getString(rowIndex, colIndex);
            if (tmp.equals("")) {
                tmp = "0";
            }
            return Double.parseDouble(tmp);
        }
    }

    /**
     * 检查列名称是否存在
     *
     * @param colName 列名称
     * @throws Exception
     */
    private void checkColName(String colName) throws Exception {
        colName = colName.toUpperCase();
        if (!this.htNameNum.containsKey(colName)) {
            throw new Exception("列 " + colName + " 不存在，请检查列名称!");
        }
    }

    /**
     * 获取Object型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colName  列名称
     * @return Object
     * @throws Exception
     * @throws Exception
     */
    public Object getObject(int rowIndex, String colName) throws Exception {
        return this.getObject(rowIndex, getColumnNo(colName));
    }

    /**
     * 获取String型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colName  列名称
     * @return String
     * @throws Exception
     * @throws Exception
     */
    public String getString(int rowIndex, String colName) throws Exception {
        return this.getString(rowIndex, getColumnNo(colName));
    }

    /**
     * 获取int型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colName  列名称
     * @return int
     * @throws Exception
     * @throws Exception
     */
    public int getInt(int rowIndex, String colName) throws Exception {
        return this.getInt(rowIndex, getColumnNo(colName));
    }

    /**
     * 获取double型数据
     *
     * @param rowIndex 行号(从0开始排)
     * @param colName  列名称
     * @return double
     * @throws Exception
     * @throws Exception
     */

    public double getDouble(int rowIndex, String colName) throws Exception {
        return this.getDouble(rowIndex, getColumnNo(colName));
    }

    /**
     * 直接输出时的结果
     *
     * @return 字符串型的数据
     */
    public String toString() {
        try {
            StringBuilder tmpStr = new StringBuilder("列名->列号：");
            tmpStr.append(this.htNameNum.toString()).append("\r\n");
            tmpStr.append("字段信息：{\r\n");
            for (int i = 0; i < this.alField.size() - 1; i++) {
                tmpStr.append("列").append(i).append("：[").append(this.alField.get(i).toString()).append("]\r\n");
            }
            if (this.alField.size() > 0) {
                tmpStr.append("列").append(this.alField.size() - 1).append("：[").append(this.alField.get(this.alField.size() - 1).toString()).append("]");
            }
            tmpStr.append("}\r\n");
            tmpStr.append("数据：{\r\n");
            for (int i = 0; i < this.alData.size(); i++) {
                for (int j = 0; j < this.getRow(i).size() - 1; j++) {
                    tmpStr.append(this.getString(i, j)).append(",");
                }
                if (this.getRow(i).size() > 0) {
                    tmpStr.append(this.getString(i, this.getRow(i).size() - 1)).append("\r\n");
                }
            }
            tmpStr.append("}");
            return tmpStr.toString();
        } catch (Exception ex) {
            return "toString方法执行时发生错误:" + ex;
        }
    }

    /**
     * 直接输出时的结果
     *
     * @param out PrintStream
     */
    public void print(PrintStream out) {
        try {
            out.print("列名->列号：");
            out.print(this.htNameNum.toString() + "\r\n");
            out.print("字段信息：{\r\n");
            for (int i = 0; i < this.alField.size() - 1; i++) {
                out.print("列" + i + "：[" + this.alField.get(i).toString() + "]\r\n");
            }
            if (this.alField.size() > 0) {
                out.print("列" + (this.alField.size() - 1) + "：["
                        + this.alField.get(this.alField.size() - 1).toString() + "]");
            }
            out.print("}\r\n");
            out.print("数据：{\r\n");
            for (int i = 0; i < this.alData.size(); i++) {
                for (int j = 0; j < this.getRow(i).size() - 1; j++) {
                    out.print(this.getString(i, j) + ",");
                }
                if (this.getRow(i).size() > 0) {
                    out.print(this.getString(i, this.getRow(i).size() - 1) + "\r\n");
                }
            }
            out.print("}");
        } catch (Exception ex) {
            out.print("toString方法执行时发生错误:" + ex);
        }
    }

    /**
     * 增加一条记录
     *
     * @param alRow 要插入的行
     * @throws Exception
     * @throws Exception
     */
    public void addRow(ArrayList<Object> alRow) {
        alData.add(alRow);
    }

    /**
     * 获取某行
     *
     * @param rowIndex 行号
     * @return void
     * @throws Exception
     * @throws Exception
     */
    public ArrayList<Object> getRow(int rowIndex) {
        return alData.get(rowIndex);
    }

    /**
     * 清除某一行
     *
     * @param rowIndex 移除位置
     */
    public void removeRow(int rowIndex) {
        alData.remove(rowIndex);
    }

    /**
     * 将另一个DataSet直接追加到该DataSet中
     *
     * @param ds DataSet
     */
    public void append(DataSet ds) {
        this.alData.addAll(ds.alData);
    }

    /**
     * 判断是否无数据（为空）
     *
     * @return boolean 是否为空
     */
    public boolean isEmpty() {
        return alData.isEmpty();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        alData.clear();
        alField.clear();
        htNameNum.clear();
    }

    /**
     * 克隆
     *
     * @return Object 对象
     * @throws CloneNotSupportedException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object clone() throws CloneNotSupportedException {
        DataSet obj = (DataSet) super.clone();
        obj.alData = (ArrayList) alData.clone();
        obj.alField = (ArrayList) alField.clone();
        obj.htNameNum = (HashMap) htNameNum.clone();
        obj.colCount = colCount;
        obj.tmprowIndex = tmprowIndex;
        obj.columnType = columnType.clone();
        return obj;
    }

    /**
     * 排序
     *
     * @param sortType int[][]，排序列号、排序类型
     * @throws Exception
     */
    public void sort(int[][] sortType) throws Exception {
        for (int[] aSortType : sortType) {
            int type = aSortType[1];
            if (!(type == 0 || type == 1)) {
                throw new Exception("排序方式：0，升序；1，降序。");
            }
        }

        // 增加列
        int newSortType[][] = new int[sortType.length][2];
        for (int k = 0; k < newSortType.length; k++) {
            newSortType[k][1] = sortType[k][1];
            DataField tmpField = (DataField) (this.getDataField(sortType[k][0]).clone());
            tmpField.setName(tmpField.getName() + "Sort");
            newSortType[k][0] = this.addField(tmpField);
            for (int i = 0; i < this.alData.size(); i++) {
                this.getRow(i).add(this.getObject(i, sortType[k][0]));
            }
        }

        RowComparator rc = new RowComparator(newSortType, this.columnType);
        // RowComparator rc = new RowComparator(sortType, this.columnType);
        Collections.sort(this.alData, rc);

        // 删除列
        for (int k = newSortType.length - 1; k > -1; k--) {
            this.removeField(newSortType[k][0]);
        }
    }

    /**
     * 排序
     *
     * @param sortType String[][],排序列号、排序类型（ASC，升序；DESC，降序）
     * @throws Exception
     */
    public void sort(String[][] sortType) throws Exception {
        int a[][] = new int[sortType.length][2];
        for (int i = 0; i < a.length; i++) {
            String type = sortType[i][1].toUpperCase();
            if (!(type.equals("ASC") || type.equals("DESC"))) {
                throw new Exception("排序方式：ASC，升序；DESC，降序。");
            }
            a[i][0] = this.getColumnNo(sortType[i][0]);
            a[i][1] = 0;
            if (type.equals("DESC")) {
                a[i][1] = 1;
            }
        }
        sort(a);
    }

    public void setFileEncoding(String fileEncoding) {
        this.fileEncoding = fileEncoding;
    }
}