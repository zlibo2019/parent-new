package com.weds.bean.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
// @ComponentScan({"com.weds.bean.datasource"})
@ConditionalOnProperty(name = "spring.datasource.multiple", matchIfMissing = true, havingValue = "false")
@EnableConfigurationProperties(DruidParams.class)
public class DruidConfig {//
    // implements TransactionManagementConfigurer {
    @Resource
    private DruidParams druidParams;

    // @Resource
    // private PlatformTransactionManager platformTransactionManager;

    @Bean
    @ConditionalOnBean(name = "wallConfig")
    public WallFilter wallFilter(WallConfig wallConfig) {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig);
        return wallFilter;
    }

    @Bean
    public WallConfig wallConfig() {
        WallConfig wallConfig = new WallConfig();
        wallConfig.setMultiStatementAllow(true);//允许一次执行多条语句
        wallConfig.setNoneBaseStatementAllow(true);//允许非基本语句的其他语句
        return wallConfig;
    }

    @Bean     //声明其为Bean实例
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    @ConditionalOnBean(name = "wallFilter")
    public DataSource dataSource(WallFilter wallFilter) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(druidParams.getUrl());
        datasource.setUsername(druidParams.getUsername());
        datasource.setPassword(druidParams.getPassword());
        datasource.setDriverClassName(druidParams.getDriverClassName());

        //configuration
        datasource.setInitialSize(druidParams.getInitialSize());
        datasource.setMinIdle(druidParams.getMinIdle());
        datasource.setMaxActive(druidParams.getMaxActive());
        datasource.setMaxWait(druidParams.getMaxWait());

        datasource.setTimeBetweenEvictionRunsMillis(druidParams.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(druidParams.getMinEvictableIdleTimeMillis());

        datasource.setValidationQuery(druidParams.getValidationQuery());
        datasource.setTestWhileIdle(druidParams.isTestWhileIdle());
        datasource.setTestOnBorrow(druidParams.isTestOnBorrow());
        datasource.setTestOnReturn(druidParams.isTestOnReturn());

        datasource.setPoolPreparedStatements(druidParams.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(druidParams.getMaxPoolPreparedStatementPerConnectionSize());

        datasource.setUseGlobalDataSourceStat(druidParams.isUseGlobalDataSourceStat());
        try {
            datasource.setFilters(druidParams.getFilters());
        } catch (SQLException e) {
            System.err.println("druid configuration initialization filter: " + e);
        }
        datasource.setConnectionProperties(druidParams.getConnectionProperties());

        List<Filter> filters = new ArrayList<>();
        filters.add(wallFilter);
        datasource.setProxyFilters(filters);
        return datasource;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // @Override
    // public PlatformTransactionManager annotationDrivenTransactionManager() {
    //     return platformTransactionManager;
    // }

    @Bean
    public ServletRegistrationBean druidStatViewServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        // 用户名和密码
        initParams.put("loginUsername", druidParams.getLoginUsername());
        initParams.put("loginPassword", druidParams.getLoginPassword());
        // 白名单
        initParams.put("allow", druidParams.getAllow());
        // 是否能够重置数据
        initParams.put("resetEnable", druidParams.getResetEnable());
        servletRegistrationBean.setInitParameters(initParams);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean druidWebStatFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        //添加过滤规则
        filterRegistrationBean.addUrlPatterns("/*");
        //添加不需要忽略的格式信息.
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
