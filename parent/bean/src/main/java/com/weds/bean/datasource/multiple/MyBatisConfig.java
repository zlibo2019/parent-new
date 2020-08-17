package com.weds.bean.datasource.multiple;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
// @ComponentScan({"com.weds.bean.datasource.multiple"})
@ConditionalOnProperty(name = "spring.datasource.multiple", havingValue = "true")
// @MapperScan(basePackages = MyBatisConfig.BASE_PACKAGE, sqlSessionTemplateRef = "sqlSessionTemplate")
public class MyBatisConfig extends AbstractDataSourceConfig {

    // private static final String BASE_PACKAGE = "com.weds.*.mapper";
    private static final String ALIASES_PACKAGE = "com.weds.*.entity";
    private static final String PREFIX_ONE = "spring.datasource.";
    private static final String PREFIX_TWO = "spring.datasource.two.";

    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSourceOne(Environment env) {
        return getDataSource(env, PREFIX_ONE, DataSourceKey.DS_ONE);
    }

    @Bean(name = "dataSourceTwo")
    public DataSource dataSourceTwo(Environment env) {
        return getDataSource(env, PREFIX_TWO, DataSourceKey.DS_TWO);
    }

    // @Bean(name = "dynamicDataSource")
    // public DynamicDataSource dynamicDataSource(@Qualifier("dataSource") DataSource dataSourceOne,
    //                                            @Qualifier("dataSourceTwo") DataSource dataSourceTwo) {
    //     Map<Object, Object> targetDataSources = new HashMap<>();
    //     targetDataSources.put(DataSourceKey.DS_ONE, dataSourceOne);
    //     targetDataSources.put(DataSourceKey.DS_TWO, dataSourceTwo);
    //     DynamicDataSource dataSource = new DynamicDataSource();
    //     dataSource.setTargetDataSources(targetDataSources);
    //     dataSource.setDefaultTargetDataSource(dataSourceOne);
    //     return dataSource;
    // }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryOne(@Qualifier("dataSource") DataSource dataSource, Environment env)
            throws Exception {
        return createSqlSessionFactory(dataSource, env, PREFIX_ONE);
    }

    @Bean(name = "sqlSessionFactoryTwo")
    public SqlSessionFactory sqlSessionFactoryTwo(@Qualifier("dataSourceTwo") DataSource dataSource, Environment env)
            throws Exception {
        return createSqlSessionFactory(dataSource, env, PREFIX_TWO);
    }

    @Bean(name = "sqlSessionTemplate")
    public CustomSqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory factoryOne,
                                                       @Qualifier("sqlSessionFactoryTwo") SqlSessionFactory factoryTwo) throws Exception {
        Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        sqlSessionFactoryMap.put(DataSourceKey.DS_ONE, factoryOne);
        sqlSessionFactoryMap.put(DataSourceKey.DS_TWO, factoryTwo);
        CustomSqlSessionTemplate customSqlSessionTemplate = new CustomSqlSessionTemplate(factoryOne);
        customSqlSessionTemplate.setTargetSqlSessionFactorys(sqlSessionFactoryMap);
        return customSqlSessionTemplate;
    }

    /**
     * 创建数据源
     *
     * @param dataSource
     * @return
     */
    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource, Environment env, String prefix) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setVfs(SpringBootVFS.class);
        bean.setTypeAliasesPackage(ALIASES_PACKAGE);
        // org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session
        //         .Configuration();
        // configuration.setMapUnderscoreToCamelCase(true);
        // bean.setConfiguration(configuration);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty(prefix + "mapperLocations")));
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(env.getProperty(prefix + "configLocation")));
        return bean.getObject();
    }
}