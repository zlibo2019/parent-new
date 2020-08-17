package com.weds.core.db;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * <pre>
 * <dt><b>类名：</b></dt>
 * <dd>ConnSet</dd>
 * <dt><b>描述：</b></dt>
 * <dd>数据库连接属性</dd>
 * <dt><b>日期：</b></dt>
 * <dd>2016-5-3 上午11:25:10</dd>
 * </pre>
 *
 * @author SXM
 * @version 0.1
 */
class ConnSet implements Serializable {
    private static final long serialVersionUID = -8742072838447647162L;
    private String dbConnect;
    private String dbDriver;
    private String dbName;
    private String dbUser;
    private String dbPassword;
    private String dbCharset;
    private String jndiName;
    private int dbCheckCode;
    private int transactionIsolation;
    private Hashtable<Object, Object> mapConn;

    private boolean isSameCharset;
    private boolean isDefault;
    private boolean isDB2;
    private boolean isInformix;
    private boolean isMySQL;
    private boolean isODBC;
    private boolean isOracle;
    private boolean isSQLServer;
    private boolean isSybase;
    private boolean isOtherDataSource;

    public ConnSet() {
        dbConnect = "";
        dbDriver = "";
        dbName = "";
        dbUser = "";
        dbPassword = "";
        dbCharset = "GBK";
        dbCheckCode = 42704;
        jndiName = "";
        transactionIsolation = 2;

        mapConn = new Hashtable<>();

        isSameCharset = true;
        isDefault = false;
        isDB2 = false;
        isInformix = false;
        isMySQL = false;
        isODBC = false;
        isOracle = false;
        isSQLServer = false;
        isSybase = false;
        isOtherDataSource = false;
    }

    public String getDbConnect() {
        return dbConnect;
    }

    public void setDbConnect(String dbConnect) {
        this.dbConnect = dbConnect;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbCharset() {
        return dbCharset;
    }

    public void setDbCharset(String dbCharset) {
        this.dbCharset = dbCharset;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public int getDbCheckCode() {
        return dbCheckCode;
    }

    public void setDbCheckCode(int dbCheckCode) {
        this.dbCheckCode = dbCheckCode;
    }

    public int getTransactionIsolation() {
        return transactionIsolation;
    }

    public void setTransactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
    }

    public Hashtable<Object, Object> getMapConn() {
        return mapConn;
    }

    public void setMapConn(Hashtable<Object, Object> mapConn) {
        this.mapConn = mapConn;
    }

    public boolean isSameCharset() {
        return isSameCharset;
    }

    public void setSameCharset(boolean sameCharset) {
        isSameCharset = sameCharset;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isDB2() {
        return isDB2;
    }

    public void setDB2(boolean DB2) {
        isDB2 = DB2;
    }

    public boolean isInformix() {
        return isInformix;
    }

    public void setInformix(boolean informix) {
        isInformix = informix;
    }

    public boolean isMySQL() {
        return isMySQL;
    }

    public void setMySQL(boolean mySQL) {
        isMySQL = mySQL;
    }

    public boolean isODBC() {
        return isODBC;
    }

    public void setODBC(boolean ODBC) {
        isODBC = ODBC;
    }

    public boolean isOracle() {
        return isOracle;
    }

    public void setOracle(boolean oracle) {
        isOracle = oracle;
    }

    public boolean isSQLServer() {
        return isSQLServer;
    }

    public void setSQLServer(boolean SQLServer) {
        isSQLServer = SQLServer;
    }

    public boolean isSybase() {
        return isSybase;
    }

    public void setSybase(boolean sybase) {
        isSybase = sybase;
    }

    public boolean isOtherDataSource() {
        return isOtherDataSource;
    }

    public void setOtherDataSource(boolean otherDataSource) {
        isOtherDataSource = otherDataSource;
    }
}
