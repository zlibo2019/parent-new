package com.weds.core.model;


import com.weds.core.db.DataSet;
import com.weds.core.db.DbAccess;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

/**
 * 生成库表结构文档
 *
 * @author sxm
 * @date 2013-6-27
 */
public class ModelUtils {

    private int idxNum = 10;

    public static void main(String args[]) throws Exception {
        String schema = "dbo";
        String path = "E:/DB_Model/";
        String[] tableType = {"TABLE", "VIEW"};
        new ModelUtils().createTableToExcel(schema, tableType, path);
    }

    /**
     * 取得数据库表结构
     *
     * @param schema    数据库用户名
     * @param tableType 库表类型数组 如TABLE,VIEW
     * @param path      生成的excel文件存放路径
     * @throws Exception
     */
    public void createTableToExcel(String schema, String[] tableType, String path) throws Exception {
        DbAccess db = new DbAccess();
        Connection conn = db.getConnection();
        Map<String, List<ModelEntity>> table = new TreeMap<String, List<ModelEntity>>();
        String[] except = {"ACT_"};

        System.out.println("生成DB_Model开始......");
        String sql = "";

        // 获取数据库表清单
        Map<String, String> tablenames = new TreeMap<>();
        if (db.isSQLServer()) {
            sql = "SELECT DISTINCT D.NAME, CONVERT(VARCHAR(1000), F.VALUE) AS REMARKS FROM SYSCOLUMNS A " +
                    "LEFT JOIN SYSTYPES B ON A.XUSERTYPE= B.XUSERTYPE " +
                    "INNER JOIN SYSOBJECTS D ON A.ID= D.ID  AND D.XTYPE= 'U'  AND D.NAME<> 'DTPROPERTIES' " +
                    "LEFT JOIN SYSCOMMENTS E ON A.CDEFAULT= E.ID LEFT JOIN SYS.EXTENDED_PROPERTIES G ON A.ID= G.MAJOR_ID  " +
                    "AND A.COLID= G.MINOR_ID LEFT JOIN SYS.EXTENDED_PROPERTIES F ON D.ID= F.MAJOR_ID  AND F.MINOR_ID= 0";
            // where d.name = ?
            DataSet ds = db.executeQuery(sql);
            for (int i = 0; i < ds.size(); i++) {
                String tabName = ds.getString(i, "NAME");
                boolean flag = false;
                for (String str : except) {
                    if (tabName.toUpperCase().contains(str)) {
                        flag = true;
                    }
                }
                if (flag) {
                    continue;
                }
                String remark = ds.getString(i, "REMARKS");
                if (remark == null || remark.equals("")) {
                    tablenames.put(tabName, tabName);
                } else {
                    tablenames.put(tabName, remark);
                }
            }
        } else {
            ResultSet tableRs = conn.getMetaData().getTables(null, schema.toUpperCase(), null, tableType);
            // 获取数据库表清单
            while (tableRs.next()) {
                String tabName = tableRs.getString("TABLE_NAME");
                boolean flag = false;
                for (String str : except) {
                    if (tabName.toUpperCase().contains(str)) {
                        flag = true;
                    }
                }
                if (flag) {
                    continue;
                }
                String remark = tableRs.getString("REMARKS");
                if (remark == null || remark.equals("")) {
                    tablenames.put(tabName, tabName);
                } else {
                    tablenames.put(tabName, remark);
                }
            }
        }

        // 获取数据库表字段信息
        for (Map.Entry<String, String> entry : tablenames.entrySet()) {
            // 获取表主键信息
            Map<String, Integer> keymap = new LinkedHashMap<>();
            ResultSet pkRs = conn.getMetaData().getPrimaryKeys(null, schema.toUpperCase(), entry.getKey().toUpperCase());
            while (pkRs.next()) {
                if (pkRs.getObject(5) instanceof BigDecimal) {
                    int value = ((BigDecimal) pkRs.getObject(5)).intValue();
                    keymap.put((String) pkRs.getObject(4), value);
                } else {
                    keymap.put((String) pkRs.getObject(4), Integer.parseInt(pkRs.getObject(5).toString()));
                }
            }

            // 获取表索引信息
            ArrayList<ArrayList<String>> indexes = new ArrayList<>();
            for (int i = 0; i < idxNum; i++) {
                indexes.add(new ArrayList<>());
            }

            if (db.isSQLServer()) {
                ResultSet indexRs = conn.getMetaData().getIndexInfo(null, schema.toUpperCase(),
                        entry.getKey().toUpperCase(), false, false);

                String temp = "";
                int k = -1;
                while (indexRs.next()) {
                    int type = indexRs.getInt("TYPE");
                    String indexName = indexRs.getString("INDEX_NAME");
                    if (type == 1 || indexName == null) {
                        continue;
                    }
                    String columnName = indexRs.getString("COLUMN_NAME");
                    if (!indexName.equals(temp)) {
                        k++;
                    }
                    temp = indexName;
                    if (k < idxNum) {
                        indexes.get(k).add(columnName);
                    }
                }
            } else {
                if (db.isDB2()) {
                    sql = "SELECT COLNAMES AS COLUMN_NAME FROM SYSIBM.SYSINDEXES WHERE UNIQUERULE='D' AND TBNAME='"
                            + entry.getKey() + "' ORDER BY NAME";
                } else if (db.isMySQL()) {
                    sql = "SELECT NON_UNIQUE, COLUMN_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE TABLE_NAME = '"
                            + entry.getKey() + "' ORDER BY NON_UNIQUE, SEQ_IN_INDEX";
                } else if (db.isOracle()) {
                    sql = "SELECT COLUMN_NAME FROM USER_TAB_COLS WHERE TABLE_NAME = '"
                            + entry.getKey() + "' ORDER BY COLUMN_ID";
                }

                DataSet ds = db.executeQuery(sql);

                for (int i = 0; i < ds.size(); i++) {
                    String c = ds.getString(i, "COLUMN_NAME");
                    if (db.isDB2() || db.isOracle()) {
                        String cols[] = c.substring(1).split("[+]");
                        for (String col : cols) {
                            indexes.get(i).add(col);
                        }
                    } else if (db.isMySQL()) {
                        indexes.get(ds.getInt(i, "NON_UNIQUE")).add(c);
                    }
                }
            }

            // 获取表字段信息
            List<ModelEntity> fs = new ArrayList<ModelEntity>();
            ResultSet colRs = conn.getMetaData().getColumns(null, schema.toUpperCase(),
                    entry.getKey().toUpperCase(), null);
            Map<String, String> colMap = new HashMap<>();
            if (db.isSQLServer()) {
                sql = "SELECT B.NAME, CONVERT(VARCHAR(1000), C.VALUE) AS REMARKS FROM SYS.TABLES A INNER JOIN SYS.COLUMNS B " +
                        "ON B.OBJECT_ID = A.OBJECT_ID LEFT JOIN SYS.EXTENDED_PROPERTIES C ON C.MAJOR_ID = B.OBJECT_ID " +
                        "AND C.MINOR_ID = B.COLUMN_ID WHERE A.NAME = '" + entry.getKey() + "'";
                DataSet ds = db.executeQuery(sql);
                for (int i = 0; i < ds.size(); i++) {
                    colMap.put(ds.getString(i, "NAME").toUpperCase(), ds.getString(i, "REMARKS"));
                }
            }

            while (colRs.next()) {
                ModelEntity dmu = new ModelEntity();
                dmu.setTablename(entry.getKey());
                dmu.setTablename_cn(entry.getValue() == null ? "" : entry.getValue());
                dmu.setEnname(colRs.getString("COLUMN_NAME").toUpperCase());

                if (db.isSQLServer()) {
                    String remark = colMap.get(colRs.getString("COLUMN_NAME").toUpperCase());
                    dmu.setCnname(remark);
                } else {
                    dmu.setCnname(colRs.getString("REMARKS"));
                }

                String sqlType = colRs.getString("TYPE_NAME").toUpperCase();
                dmu.setSqltype(sqlType);

                if (sqlType.contains("INT") || sqlType.equals("DATETIME") || sqlType.equals("TEXT")) {
                    dmu.setSize(0);
                } else {
                    dmu.setSize(colRs.getInt("COLUMN_SIZE"));
                }

                if (sqlType.equals("DATETIME")) {
                    dmu.setJingdu(0);// 小数的位数
                } else {
                    dmu.setJingdu(colRs.getInt("DECIMAL_DIGITS"));// 小数的位数
                }

                dmu.setIsnull(colRs.getInt("NULLABLE"));// 是否为空
                for (Map.Entry<String, Integer> key : keymap.entrySet()) {
                    if (colRs.getString("COLUMN_NAME").toUpperCase().equals(key.getKey().toUpperCase())) {
                        dmu.setIspk(key.getValue());// 是否为主键
                    }
                }

                // 判断索引,最多10个
                for (int y = 0; y < idxNum; y++) {
                    for (int j = 0; j < indexes.get(y).size(); j++) {
                        if (dmu.getEnname().equals(indexes.get(y).get(j))) {
                            if (y == 0)
                                dmu.setIssy0(j + 1);
                            if (y == 1)
                                dmu.setIssy1(j + 1);
                            if (y == 2)
                                dmu.setIssy2(j + 1);
                            if (y == 3)
                                dmu.setIssy3(j + 1);
                            if (y == 4)
                                dmu.setIssy4(j + 1);
                            if (y == 5)
                                dmu.setIssy5(j + 1);
                            if (y == 6)
                                dmu.setIssy6(j + 1);
                            if (y == 7)
                                dmu.setIssy7(j + 1);
                            if (y == 8)
                                dmu.setIssy8(j + 1);
                            if (y == 9)
                                dmu.setIssy9(j + 1);
                        }
                    }
                }
                fs.add(dmu);
            }
            table.put(entry.getKey() + "," + entry.getValue(), fs);
        }
        path += "DB_Model" + System.currentTimeMillis() + ".xls";
        ModelExcel.excelrw(table, path);
        System.out.println("生成DB_Model结束!");
    }
}
