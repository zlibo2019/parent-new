package com.weds.bean.datasource.multiple;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.microsoft.sqlserver.jdbc.SQLServerXADataSource;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 针对springboot的数据源配置
 */
public abstract class AbstractDataSourceConfig {

    public static final String SQL_SERVER = "sqlserver";

    protected DataSource getDataSource(Environment env, String prefix, String dataSourceName) {
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName(dataSourceName);
        String driverClassName = env.getProperty(prefix + "driverClassName", "");
        if (driverClassName.contains(SQL_SERVER)) {
            SQLServerXADataSource xaDataSource = new SQLServerXADataSource();
            xaDataSource.setURL(env.getProperty(prefix + "url"));
            xaDataSource.setUser(env.getProperty(prefix + "username"));
            xaDataSource.setPassword(env.getProperty(prefix + "password"));
            ds.setXaDataSource(xaDataSource);
        } else {
            Properties prop = build(env, prefix);
            ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
            ds.setXaProperties(prop);
        }
        ds.setMaxPoolSize(env.getProperty(prefix + "maxActive", Integer.class));
        ds.setMinPoolSize(env.getProperty(prefix + "minIdle", Integer.class));
        return ds;
    }

    protected Properties build(Environment env, String prefix) {
        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driverClassName", ""));
        prop.put("initialSize", env.getProperty(prefix + "initialSize", Integer.class));
        prop.put("maxActive", env.getProperty(prefix + "maxActive", Integer.class));
        prop.put("minIdle", env.getProperty(prefix + "minIdle", Integer.class));
        prop.put("maxWait", env.getProperty(prefix + "maxWait", Integer.class));
        prop.put("timeBetweenEvictionRunsMillis", env.getProperty(prefix + "timeBetweenEvictionRunsMillis", Integer.class));
        prop.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "minEvictableIdleTimeMillis", Integer.class));
        prop.put("validationQuery", env.getProperty(prefix + "validationQuery"));
        prop.put("testOnBorrow", env.getProperty(prefix + "testOnBorrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "testOnReturn", Boolean.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "testWhileIdle", Boolean.class));
        prop.put("poolPreparedStatements", env.getProperty(prefix + "poolPreparedStatements", Boolean.class));
        prop.put("maxPoolPreparedStatementPerConnectionSize", env.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class));
        prop.put("useGlobalDataSourceStat", env.getProperty(prefix + "useGlobalDataSourceStat", Boolean.class));
        // prop.put("validationQueryTimeout", env.getProperty(prefix + "validationQueryTimeout", Integer.class));
        prop.put("filters", env.getProperty(prefix + "filters"));
        prop.put("connectionProperties", env.getProperty(prefix + "connectionProperties"));
        return prop;
    }
}
