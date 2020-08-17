package com.weds.core.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * <pre>
 * <dt><b>类名：</b></dt>
 * <dd>DbAccess</dd>
 * <dt><b>描述：</b></dt>
 * <dd>数据库操作类</dd>
 * <dt><b>日期：</b></dt>
 * <dd>2016-5-3 上午11:05:22</dd>
 * </pre>
 *
 * @author SXM
 * @version 0.1
 */
public class DbAccess {
    private Logger log = LogManager.getLogger();

    private ConnSet cs;// 数据库连接
    private HashMap<String, Object> mapPreparePara;
    private Connection currentConnection;
    private PreparedStatement ps;
    private Statement stmt;

    private static Hashtable<String, ConnSet> mapNameConnSet = new Hashtable<>();// 数据库连接池
    private static boolean instantiation = false;// 是否已经实例化
    private static int maxConnCount = 20;
    private static int timeout = 0;

    private boolean autoCommit;
    private String fileEncoding;
    private String dbName;

    /**
     * @param dbName 数据源名称
     * @throws Exception
     */
    public DbAccess(String dbName) throws Exception {
        cs = null;
        currentConnection = null;
        stmt = null;
        ps = null;
        mapPreparePara = null;
        autoCommit = true;
        this.dbName = dbName.toLowerCase();
        init();
    }

    /**
     * @throws Exception
     */
    public DbAccess() throws Exception {
        cs = null;
        currentConnection = null;
        stmt = null;
        ps = null;
        mapPreparePara = null;
        dbName = "";
        autoCommit = true;
        init();
    }

    /**
     * @param b 事务开始标志
     * @throws Exception
     */
    public DbAccess(boolean b) throws Exception {
        cs = null;
        currentConnection = null;
        stmt = null;
        ps = null;
        mapPreparePara = null;
        dbName = "";
        autoCommit = b;
        init();
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>initByProperties</dd>
     * <dt><b>描述：</b></dt>
     * <dd>初始化数据源连接</dd>
     * </pre>
     *
     * @throws Exception
     */
    private void initByProperties() throws Exception {
        InputStream is = getClass().getResourceAsStream(DbParams.DB_PROPERTIES);
        if (is == null) {
            throw new Exception("file conn.properties not found!");
        }
        Properties p = new Properties();
        p.load(is);

        fileEncoding = p.getProperty(DbParams.FILE_ENCODING);
        maxConnCount = Integer.parseInt(p.getProperty(DbParams.CONN_COUNT));
        timeout = Integer.parseInt(p.getProperty(DbParams.TIME_OUT));
        String allDbName = p.getProperty(DbParams.ALL_DB_NAME);

        StringTokenizer st = new StringTokenizer(allDbName, DbParams.DB_SPLIT, false);
        while (st.hasMoreTokens()) {
            ConnSet connSet = new ConnSet();
            String dbName = st.nextToken().toLowerCase();
            connSet.setDbName(dbName);

            if (mapNameConnSet.containsKey(dbName)) {
                throw new Exception(dbName + " is exists");
            }

            String temp = p.getProperty(dbName + DbParams.IS_DEFAULT);
            if (temp != null && temp.equals("true")) {
                connSet.setDefault(true);
            } else {
                temp = p.getProperty(dbName + DbParams.IS_OTHER_DATASOURCE);
                if (temp != null && temp.equals("true")) {
                    connSet.setOtherDataSource(true);
                }
            }

            temp = p.getProperty(dbName + DbParams.TRANSACTION_ISOLATION);
            if (temp != null) {
                connSet.setTransactionIsolation(Integer.parseInt(temp));
            }

            temp = p.getProperty(dbName + DbParams.DB_CHARSET);
            if (temp != null) {
                connSet.setDbCharset(temp);
            }

            if (connSet.isOtherDataSource()) {
                temp = p.getProperty(dbName + DbParams.JNDI_NAME);
                if (temp != null) {
                    connSet.setJndiName(temp);
                }
            } else {
                temp = p.getProperty(dbName + DbParams.DB_DRIVER);
                if (temp != null) {
                    connSet.setDbDriver(temp);
                }
                temp = p.getProperty(dbName + DbParams.DB_CONNECT);
                if (temp != null) {
                    connSet.setDbConnect(temp);
                }
                temp = p.getProperty(dbName + DbParams.DB_USER);
                if (temp != null) {
                    connSet.setDbUser(temp);
                }
                temp = p.getProperty(dbName + DbParams.DB_PASSWORD);
                if (temp != null) {
                    connSet.setDbPassword(temp);
                }
            }

            connSet.setSQLServer(connSet.getDbDriver().toLowerCase().contains("sqlserver."));
            connSet.setInformix(connSet.getDbDriver().toLowerCase().contains("informix."));
            connSet.setDB2(connSet.getDbDriver().toLowerCase().contains("db2."));
            connSet.setOracle(connSet.getDbDriver().toLowerCase().contains("oracle."));
            connSet.setMySQL(connSet.getDbDriver().toLowerCase().contains("mysql."));
            connSet.setSybase(connSet.getDbDriver().toLowerCase().contains("sybase."));
            connSet.setODBC(connSet.getDbDriver().toLowerCase().contains("odbc."));
            mapNameConnSet.put(dbName, connSet);
        }
        is.close();
        instantiation = true;
    }

    private void init() throws Exception {
        if (cs == null) {
            if (!instantiation) {
                initByProperties();
            }
            cs = findConnSetBydbName();// 获得当数据库连接信息
            setDbName(cs.getDbName());
            cs.setSameCharset(cs.getDbCharset().equalsIgnoreCase(fileEncoding));
            if (!cs.isOtherDataSource()) {
                Class.forName(cs.getDbDriver());
            }
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>RStoDS</dd>
     * <dt><b>描述：</b></dt>
     * <dd>根据开始及结束行位置将数据集转化为DataSet</dd>
     * </pre>
     *
     * @param rs       数据集结果
     * @param startRow 开始行数
     * @param rowCount 获取行数
     * @return
     * @throws Exception
     */
    private DataSet RStoDS(ResultSet rs, int startRow, int rowCount) throws Exception {
        ResultSetMetaData rsmd = rs.getMetaData();
        int numCols = rsmd.getColumnCount();
        DataField dataField[] = new DataField[numCols];
        int dataType[] = new int[numCols];
        for (int i = 0; i < numCols; i++) {
            dataField[i] = new DataField();
            dataField[i].setName(rsmd.getColumnName(i + 1));
            dataField[i].setAlias(rsmd.getColumnName(i + 1));
            dataField[i].setSqlType(rsmd.getColumnTypeName(i + 1));// 数据库中对应的数据类型
            dataField[i].setClassName(rsmd.getColumnClassName(i + 1));// 对应的java类型
            dataField[i].setDisplaySize(rsmd.getColumnDisplaySize(i + 1));// 字段长度
            dataField[i].setScale(rsmd.getScale(i + 1));
            dataType[i] = DataField.getColumnTypeByClassName(dataField[i].getClassName());
            dataField[i].setType(dataType[i]);
        }

        int cnt = -1;
        DataSet dsData = new DataSet(dataField, fileEncoding);
        while (rs.next()) {
            cnt++;
            if (rowCount > 0) {
                if (cnt < startRow) {
                    continue;
                }

                if (cnt >= startRow + rowCount) {
                    break;
                }
            }
            ArrayList<Object> columns = createDataRow(rs, dataType, numCols);
            dsData.addRow(columns);
        }
        return dsData;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>addDBConfig</dd>
     * <dt><b>描述：</b></dt>
     * <dd>添加数据源信息</dd>
     * </pre>
     *
     * @param dbName               数据源名称
     * @param dbDriver             数据源驱动
     * @param dbConnect            数据源连接地址
     * @param transactionIsolation 事务隔离级别
     * @param dbUser               数据库用户名
     * @param dbPassword           数据库密码
     * @param dbCharset            数据库字符集
     */
    public void addDBConfig(String dbName, String dbDriver, String dbConnect, int transactionIsolation,
                            String dbUser, String dbPassword, String dbCharset) {
        removeDBConfig(dbName);
        ConnSet connSet = new ConnSet();
        connSet.setDbConnect(dbConnect);
        connSet.setDbDriver(dbDriver);
        connSet.setDbName(dbName);
        connSet.setDbUser(dbUser);
        connSet.setDbPassword(dbPassword);
        connSet.setDbCharset(dbCharset);
        connSet.setTransactionIsolation(transactionIsolation);

        connSet.setSameCharset(connSet.getDbCharset().equalsIgnoreCase(fileEncoding));
        connSet.setSQLServer(connSet.getDbDriver().toLowerCase().contains("sqlserver."));
        connSet.setInformix(connSet.getDbDriver().toLowerCase().contains("informix."));
        connSet.setDB2(connSet.getDbDriver().toLowerCase().contains("db2."));
        connSet.setOracle(connSet.getDbDriver().toLowerCase().contains("oracle."));
        connSet.setMySQL(connSet.getDbDriver().toLowerCase().contains("mysql."));
        connSet.setSybase(connSet.getDbDriver().toLowerCase().contains("sybase."));
        connSet.setODBC(connSet.getDbDriver().toLowerCase().contains("odbc."));
        mapNameConnSet.put(dbName.toLowerCase(), connSet);
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>beginTransaction</dd>
     * <dt><b>描述：</b></dt>
     * <dd>开启数据库事务</dd>
     * </pre>
     *
     * @throws SQLException
     */
    public void beginTransaction() throws SQLException {
        autoCommit = false;
        if (currentConnection != null) {
            currentConnection.setAutoCommit(false);
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>cancelQuery</dd>
     * <dt><b>描述：</b></dt>
     * <dd>取消数据库查询</dd>
     * </pre>
     *
     * @throws Exception
     */
    public void cancelQuery() throws Exception {
        if (stmt != null) {
            stmt.cancel();
        }
        if (ps != null) {
            ps.cancel();
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>checkConnection</dd>
     * <dt><b>描述：</b></dt>
     * <dd>检查数据库连接</dd>
     * </pre>
     *
     * @param cc 数据库连接对象
     * @return
     */
    private boolean checkConnection(Connection cc) {
        if (cc == null) {
            return false;
        }
        if (!autoCommit) {
            return true;
        }
        try {
            Statement statement = cc.createStatement();
            String sql;
            if (cs.isDB2()) {
                sql = "SELECT * FROM SYSCAT.TABLES";
            } else if (cs.isMySQL()) {
                sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES";
            } else if (cs.isSQLServer()) {
                sql = "SELECT * FROM SYS.TABLES";
            } else {
                sql = "select COUNT(*) FROM lgbConTest1213";
            }
            statement.executeQuery(sql);
            statement.close();
            return true;
        } catch (SQLException e) {
            return Integer.valueOf(e.getSQLState()) == cs.getDbCheckCode();
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>clearPool</dd>
     * <dt><b>描述：</b></dt>
     * <dd>清空数据库连接池</dd>
     * </pre>
     */
    public void clearPool() {
        ConnSet aObjSet[] = mapNameConnSet.values().toArray(new ConnSet[0]);
        try {
            for (ConnSet connSet : aObjSet) {
                Object aObjConn[] = connSet.getMapConn().keySet().toArray();
                Connection aConn[] = new Connection[aObjConn.length];
                for (int j = 0; j < aObjConn.length; j++) {
                    aConn[j] = (Connection) aObjConn[j];
                }

                for (int j = 0; j < aConn.length; j++) {
                    if (!aConn[j].getAutoCommit()) {
                        aConn[j].rollback();
                    }
                    aConn[j].close();
                    connSet.getMapConn().remove(aConn[j]);
                    aConn[j] = null;
                }
            }
            mapNameConnSet.clear();
        } catch (SQLException e) {
            String msg = "\r\n  Operation:  clear pool\r\n  Exception:   " + e.toString();
            log.error(msg);
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>closeStatement</dd>
     * <dt><b>描述：</b></dt>
     * <dd>关闭数据库Statement对象</dd>
     * </pre>
     *
     * @throws SQLException
     */
    private void closeStatement() throws SQLException {
        if (stmt != null) {
            stmt.close();
            stmt = null;
        }
        if (isPrepared()) {
            mapPreparePara = null;
            ps.close();
            ps = null;
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>commit</dd>
     * <dt><b>描述：</b></dt>
     * <dd>数据库提交</dd>
     * </pre>
     *
     * @throws Exception
     */
    public void commit() throws Exception {
        currentConnection.commit();
        freeConnection();
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>createDataRow</dd>
     * <dt><b>描述：</b></dt>
     * <dd>获取数据行信息</dd>
     * </pre>
     *
     * @param rs       数据集结果
     * @param dataType 数据列类型
     * @param numCols  数据列个数
     * @return
     * @throws Exception
     */
    private ArrayList<Object> createDataRow(ResultSet rs, int dataType[], int numCols) throws Exception {
        ArrayList<Object> row = new ArrayList<Object>();
        for (int j = 1; j <= numCols; j++) {
            switch (dataType[j - 1]) {
                case 1: // String Clob
                    if (cs.isODBC()) {
                        row.add(rs.getBytes(j));
                        break;
                    }

                    String tmp = rs.getString(j);
                    if (tmp == null) {
                        tmp = "";
                    }

                    // if (!cs.isSameCharset) {
                    row.add(tmp.getBytes(cs.getDbCharset()));
                    // } else {
                    // row.add(tmp.getBytes());
                    // }
                    break;
                case 2: // Integer Byte Short
                    row.add(rs.getInt(j));
                    break;

                case 3: // Double Float
                    row.add(rs.getDouble(j));
                    break;

                case 4:// Long BigInteger
                    row.add(rs.getLong(j));
                    break;

                case 5: // BigDecimal
                    BigDecimal bd = rs.getBigDecimal(j);
                    if (bd == null) {
                        row.add(null);
                    } else {
                        row.add(String.valueOf(bd).getBytes());
                    }
                    break;

                case 6: // Date
                    row.add(rs.getDate(j));
                    break;

                case 7: // Time
                    row.add(rs.getTime(j));
                    break;

                case 8: // Timestamp
                    row.add(rs.getTimestamp(j));
                    break;

                case 9: // BLOB
                    row.add(rs.getBytes(j));
                    break;

                default:
                    row.add(rs.getObject(j));
                    break;
            }
        }
        return row;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>executeQuery</dd>
     * <dt><b>描述：</b></dt>
     * <dd>执行数据库查询</dd>
     * </pre>
     *
     * @param sql      SQL语句
     * @param startRow 开始行数
     * @param rowCount 获取行数
     * @return
     * @throws Exception
     */
    private DataSet executeQuery(String sql, int startRow, int rowCount) throws Exception {
        DataSet ds;
        try {
            boolean isFirstTime = currentConnection == null;
            if (isFirstTime)
                currentConnection = getConnection();
            if (!cs.isSameCharset()) {
                sql = new String(sql.getBytes(fileEncoding), cs.getDbCharset());
            }
            ResultSet rs;
            try {
                if (isPrepared()) {
                    rs = ps.executeQuery();
                } else {
                    stmt = currentConnection.createStatement();
                    log.debug("querySql=" + sql);
                    rs = stmt.executeQuery(sql);
                }
            } catch (SQLException se) {
                if (!isFirstTime || isPrepared()
                        || checkConnection(currentConnection))
                    throw getBusinessException(se);
                if (cs.isOtherDataSource()) {
                    currentConnection = getConnection();
                } else {
                    cs.getMapConn().remove(currentConnection);
                    currentConnection = makeConnection();
                    cs.getMapConn().put(currentConnection, "1");
                    currentConnection.setAutoCommit(autoCommit);
                }
                stmt = currentConnection.createStatement();
                log.debug("querySql=" + sql);
                // System.out.println(sql);
                rs = stmt.executeQuery(sql);
            }
            ds = RStoDS(rs, startRow, rowCount);
            rs.close();
            closeStatement();
        } catch (Exception ex) {
            String msg = "\r\n  Operation: Query\r\n        SQL: " + sql + "\r\n";
            if (isPrepared()) {
                msg = msg + "       Data: " + mapPreparePara.toString() + "\r\n";
                String a[] = sql.replace('?', '@').split("@");
                StringBuilder sqlNew = new StringBuilder();
                for (int i = 0; i < a.length - 1; i++) {
                    sqlNew.append(a[i]).append("'").append(mapPreparePara.get(String.valueOf(i + 1))).append("'");
                }
                sqlNew.append(a[a.length - 1]);
                msg = msg + "        SQL: " + sqlNew + "\r\n";
            }
            msg = msg + "  Exception: " + ex.toString();
            if (!cs.isSameCharset()) {
                msg = new String(msg.getBytes(cs.getDbCharset()), fileEncoding);
            }
            log.error(msg);
            throw ex;
        } finally {
            if (autoCommit) {
                freeConnection();
            }
        }
        return ds;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>executePageQuery</dd>
     * <dt><b>描述：</b></dt>
     * <dd>执行数据库查询（分页）</dd>
     * </pre>
     *
     * @param sql        SQL语句
     * @param cMultiPage 分页对象
     * @return
     * @throws Exception
     */
    public DataSet executePageQuery(String sql, MultiPage cMultiPage) throws Exception {
        if (cMultiPage == null) {
            throw new Exception("类名不能为空或null");
        }

        if (cs.isDB2() || cs.isMySQL() || cs.isOracle()) {
            DataSet ds = executeQuery("SELECT COUNT(*) NUM FROM ( " + sql
                    + ") TAB");
            cMultiPage.setTotalCount(ds.getInt(0, "NUM"));
        } else {
            throw new Exception("本系统不支持该数据库");
        }

        int pageNum = cMultiPage.getPageNum();
        int startRow = (pageNum - 1) * cMultiPage.getNumPerPage();
        int rownum = cMultiPage.getNumPerPage();
        return executeQuery(sql, startRow, rownum);
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>executeQuery</dd>
     * <dt><b>描述：</b></dt>
     * <dd>执行数据库查询</dd>
     * </pre>
     *
     * @param sql SQL语句
     * @return
     * @throws Exception
     */
    public DataSet executeQuery(String sql) throws Exception {
        return executeQuery(sql, 0, 0);
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>executePrepareUpdate</dd>
     * <dt><b>描述：</b></dt>
     * <dd>执行更新操作（参数占位符）</dd>
     * </pre>
     *
     * @return
     * @throws Exception
     */
    public int executePrepareUpdate() throws Exception {
        return executeUpdate(null);
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>executeUpdate</dd>
     * <dt><b>描述：</b></dt>
     * <dd>执行更新操作</dd>
     * </pre>
     *
     * @param sql SQL语句
     * @return
     * @throws Exception
     */
    public int executeUpdate(String sql) throws Exception {
        boolean isFirstTime;
        int r;
        isFirstTime = currentConnection == null;
        String msg = null;
        if (isFirstTime) {
            currentConnection = getConnection();
        }
        if (sql != null && !cs.isSameCharset()) {
            sql = new String(sql.getBytes(fileEncoding), cs.getDbCharset());
        }
        try {
            if (isPrepared()) {
                r = ps.executeUpdate();
            } else {
                stmt = currentConnection.createStatement();
                log.debug("updateSql=" + sql);
                r = stmt.executeUpdate(sql);
            }
        } catch (SQLException ex) {
            msg = "\r\n  Operation: Update\r\n        SQL: " + sql + "\r\n";
            if (isPrepared()) {
                msg = msg + "       Data: " + mapPreparePara.toString() + "\r\n";
                String a[] = sql.replace('?', '@').split("@");
                StringBuilder sqlNew = new StringBuilder();
                for (int i = 0; i < a.length - 1; i++) {
                    sqlNew.append(a[i]).append("'").append(mapPreparePara.get(String.valueOf(i + 1))).append("'");
                }
                sqlNew.append(a[a.length - 1]);
                msg = msg + "        SQL: " + sqlNew + "\r\n";
            }
            if (!cs.isSameCharset()) {
                msg = new String(msg.getBytes(cs.getDbCharset()), fileEncoding);
            }
            log.error(msg);
            // if(!isFirstTime || isPrepared() ||
            // checkConnection(currentConnection))
            throw getBusinessException(ex);
        } catch (Exception ex) {
            msg = msg + "  Exception: " + ex.toString();
            if (!cs.isSameCharset()) {
                msg = new String(msg.getBytes(cs.getDbCharset()), fileEncoding);
            }
            log.error(msg);
            throw ex;
        } finally {
            closeStatement();
            if (autoCommit) {
                freeConnection();
            }
        }
        return r;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>existDatabase</dd>
     * <dt><b>描述：</b></dt>
     * <dd>判断数据源是否存在</dd>
     * </pre>
     *
     * @param dbName 数据源名称
     * @return
     */
    public static boolean existDatabase(String dbName) {
        return mapNameConnSet.containsKey(dbName.toLowerCase());
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>findConnSetBydbName</dd>
     * <dt><b>描述：</b></dt>
     * <dd>通过数据名称获取数据连接信息</dd>
     * </pre>
     *
     * @return
     * @throws Exception
     */
    private ConnSet findConnSetBydbName() throws Exception {
        ConnSet obj = mapNameConnSet.get(dbName);
        if (obj == null) {
            ConnSet aObjSet[] = mapNameConnSet.values().toArray(new ConnSet[0]);
            for (ConnSet connSet : aObjSet) {
                if (connSet.isDefault()) {
                    dbName = connSet.getDbName();
                    return connSet;
                }
            }
            String msg = "\r\n  Operation:  init\r\n  Exception: init, datasource name ["
                    + dbName + "] not found.";
            log.error(msg);
            throw new Exception("初始化,未找到数据源[" + dbName + "].");
        } else {
            return obj;
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>freeConnection</dd>
     * <dt><b>描述：</b></dt>
     * <dd>释放数据源连接</dd>
     * </pre>
     *
     * @throws SQLException
     */
    public synchronized void freeConnection() throws SQLException {
        closeStatement();
        if (cs.isOtherDataSource()) {
            currentConnection.close();
            return;
        }
        if (currentConnection == null) {
            return;
        }
        try {
            if (!autoCommit) {
                currentConnection.rollback();
            }
        } catch (Exception ex) {
            autoCommit = true;
            if (!currentConnection.isClosed()) {
                currentConnection.setAutoCommit(true);
            }
            cs.getMapConn().put(currentConnection, "0");
            currentConnection = null;
        } finally {
            autoCommit = true;
            if (!currentConnection.isClosed()) {
                currentConnection.setAutoCommit(true);
            }
            cs.getMapConn().put(currentConnection, "0");
            currentConnection = null;
        }

    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>getActiveConns</dd>
     * <dt><b>描述：</b></dt>
     * <dd>获取活动的数据源连接信息</dd>
     * </pre>
     *
     * @return
     */
    public String getActiveConns() {
        ConnSet aObjSet[] = mapNameConnSet.values().toArray(new ConnSet[0]);
        StringBuilder tmpConn = new StringBuilder();
        if (aObjSet.length == 0) {
            tmpConn = new StringBuilder("no connection now.");
        }
        for (ConnSet anAConnSet : aObjSet) {
            tmpConn.append(anAConnSet.getDbName()).append("=----").append(anAConnSet.getMapConn()).append("----<br>\r\n");
        }
        return tmpConn.toString();
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>getBusinessException</dd>
     * <dt><b>描述：</b></dt>
     * <dd>判断数据库错误信息是否为繁忙</dd>
     * </pre>
     *
     * @param ex SQL异常信息
     * @return
     */
    private SQLException getBusinessException(SQLException ex) {
        if (cs.isInformix() && (ex.getErrorCode() == -244 || ex.getErrorCode() == -245)) {
            return new SQLException("System is busy now,Please try again later.");
        } else {
            return ex;
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>getConnection</dd>
     * <dt><b>描述：</b></dt>
     * <dd>获取数据库连接</dd>
     * </pre>
     *
     * @return
     * @throws Exception
     */
    public synchronized Connection getConnection() throws Exception {
        if (currentConnection != null) {
            return currentConnection;
        }
        if (cs.isOtherDataSource()) {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup(cs.getJndiName());
            currentConnection = ds.getConnection();
            if (cs.getTransactionIsolation() != currentConnection.getTransactionIsolation()) {
                currentConnection.setTransactionIsolation(cs.getTransactionIsolation());
            }
        } else {
            long starttime = System.currentTimeMillis();
            synchronized (DbAccess.class) {
                Object aConn[] = cs.getMapConn().keySet().toArray();
                while (currentConnection == null) {
                    int i;
                    for (i = 0; i < aConn.length; i++) {
                        if (cs.getMapConn().get(aConn[i]).toString().equals("0")) {
                            cs.getMapConn().put(aConn[i], "1");
                            currentConnection = (Connection) aConn[i];
                            break;
                        }
                    }

                    if (i < aConn.length) {
                        break;
                    }
                    if (aConn.length < maxConnCount) {
                        currentConnection = makeConnection();
                        cs.getMapConn().put(currentConnection, "1");
                        break;
                    }
                    if (timeout > 0) {
                        long overtime = System.currentTimeMillis() - starttime;
                        if (overtime >= (long) timeout) {
                            String msg = "\r\n  Operation:  connect to databse\r\n  Exception:  timeout:  value="
                                    + timeout
                                    + "ms,wait "
                                    + String.valueOf(overtime) + "ms.\r\n";
                            log.error(msg);
                            throw new Exception("连接数据库超时:  设定时间: "
                                    + String.valueOf(timeout) + "ms,等待时间: "
                                    + String.valueOf(overtime) + "ms.");
                        }
                    }
                    Thread.sleep(10L);
                }
            }
        }
        if (!autoCommit) {
            currentConnection.setAutoCommit(false);
        }
        return currentConnection;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * </pre>
     *
     * <pre>
     * <dd>getCurrentConnection</dd>
     * </pre>
     *
     * <dt><b>描述：</b></dt> <dd>获取当前数据库连接</dd> </pre>
     *
     * @return
     * @throws Exception
     */
    public synchronized Connection getCurrentConnection() throws Exception {
        if (currentConnection != null) {
            currentConnection = getConnection();
        }
        return currentConnection;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>getDBAttributeByName</dd>
     * <dt><b>描述：</b></dt>
     * <dd>根据数据源名称及数据属性名称获取属性内容</dd>
     * </pre>
     *
     * @param dbName 数据源名称
     * @return
     * @throws Exception
     */
    public ConnSet getDBAttributeByName(String dbName) {
        ConnSet aObjSet[] = mapNameConnSet.values().toArray(new ConnSet[0]);
        for (ConnSet connSet : aObjSet) {
            if (connSet.getDbName().equalsIgnoreCase(dbName)) {
                return connSet;
            }
        }
        return null;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>makeConnection</dd>
     * <dt><b>描述：</b></dt>
     * <dd>创建数据库连接</dd>
     * </pre>
     *
     * @return
     * @throws Exception
     */
    private Connection makeConnection() throws Exception {
        Connection c;
        String tmpstr = cs.getDbConnect();
        try {
            c = DriverManager.getConnection(tmpstr, cs.getDbUser(), cs.getDbPassword());
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("获取数据库连接出错");
        }
        if (cs.isInformix()) {
            Statement sm = c.createStatement();
            sm.execute("set lock mode to wait 10");
        }
        if (cs.getTransactionIsolation() != c.getTransactionIsolation()) {
            c.setTransactionIsolation(cs.getTransactionIsolation());
        }
        return c;
    }

    public void setPrepareSQL(String sql) throws Exception {
        if (!cs.isSameCharset()) {
            sql = new String(sql.getBytes(fileEncoding), cs.getDbCharset());
        }
        ps = currentConnection.prepareStatement(sql);
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>removeDBConfig</dd>
     * <dt><b>描述：</b></dt>
     * <dd>删除数据源连接信息</dd>
     * </pre>
     *
     * @param dbName 数据源名称
     */
    public static void removeDBConfig(String dbName) {
        Object[] objNames = mapNameConnSet.keySet().toArray();
        for (Object objName : objNames) {
            if (objName.toString().equalsIgnoreCase(dbName)) {
                mapNameConnSet.remove(objName);
                break;
            }
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>rightTrim</dd>
     * <dt><b>描述：</b></dt>
     * <dd>数据去空格</dd>
     * </pre>
     *
     * @param data 数据集数据
     * @return
     */
    private static String rightTrim(String data) {
        int i = data.length() - 1;
        for (; i >= 0; i--) {
            if (data.charAt(i) != ' ') {
                break;
            }
        }
        // 字符转换
        return data.substring(0, i + 1);
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>rollback</dd>
     * <dt><b>描述：</b></dt>
     * <dd>数据库回滚</dd>
     * </pre>
     *
     * @throws Exception
     */
    public void rollback() throws Exception {
        if (currentConnection != null) {
            currentConnection.rollback();
        }
        freeConnection();
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareBigDecimal</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareBigDecimal(int i, BigDecimal value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setBigDecimal(i, value);
            mapPreparePara.put(String.valueOf(i), value);
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareBinaryStream</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @param len   字节流长度
     * @throws Exception
     */
    public void setPrepareBinaryStream(int i, InputStream value, int len) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setBinaryStream(i, value, len);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareBlob</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareBlob(int i, Blob value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setBlob(i, value);
            mapPreparePara.put(String.valueOf(i), value);
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareBoolean</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareBoolean(int i, boolean value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setBoolean(i, value);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareByte</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareByte(int i, byte value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setByte(i, value);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareBytes</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareBytes(int i, byte value[]) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setBytes(i, value);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareDouble</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareDouble(int i, double value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setDouble(i, value);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareFloat</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareFloat(int i, float value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setFloat(i, value);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareInt</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareInt(int i, int value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setInt(i, value);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareLong</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareLong(int i, long value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setLong(i, value);
            mapPreparePara.put(String.valueOf(i), String.valueOf(value));
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareObject</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareObject(int i, Object value) throws Exception {
        if (ps == null) {
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        } else {
            ps.setObject(i, value);
            mapPreparePara.put(String.valueOf(i), value);
        }
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>setPrepareString</dd>
     * <dt><b>描述：</b></dt>
     * <dd>设置SQL占位符的值</dd>
     * </pre>
     *
     * @param i     占位符位置
     * @param value 占位符值
     * @throws Exception
     */
    public void setPrepareString(int i, String value) throws Exception {
        if (ps == null)
            throw new Exception("尚未设置Prepared SQL，请先调用prepareSQL(String sql)方法。");
        if (!cs.isSameCharset()) {
            value = new String(value.getBytes(fileEncoding), cs.getDbCharset());
        }
        ps.setString(i, value);
        mapPreparePara.put(String.valueOf(i), value);
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>updateConnStr</dd>
     * <dt><b>描述：</b></dt>
     * <dd>根据数据源名称更新链接名称</dd>
     * </pre>
     *
     * @param dbName    数据源名称
     * @param dbConnect 链接名称
     */
    public static void updateConnStr(String dbName, String dbConnect) {
        ConnSet aObjSet[] = mapNameConnSet.values().toArray(new ConnSet[0]);
        for (ConnSet connSet : aObjSet) {
            if (connSet.getDbName().equalsIgnoreCase(dbName)) {
                connSet.setDbConnect(dbConnect);
                break;
            }
        }
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    private boolean isPrepared() {
        return ps != null;
    }

    public boolean isDB2() {
        return cs.isDB2();
    }

    public boolean isInformix() {
        return cs.isInformix();
    }

    public boolean isMySQL() {
        return cs.isMySQL();
    }

    public boolean isODBC() {
        return cs.isODBC();
    }

    public boolean isOracle() {
        return cs.isOracle();
    }

    public boolean isSQLServer() {
        return cs.isSQLServer();
    }

    public boolean isSameCharset() {
        return cs.isSameCharset();
    }

    public boolean isSybase() {
        return cs.isSybase();
    }
}
