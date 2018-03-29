package com.zjcds.common.datasource;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.zjcds.common.datasource.properties.DruidDataSourceProperties;
import com.zjcds.common.datasource.properties.DruidStatProperties;
import com.zjcds.common.datasource.properties.HikariDataSourceProperties;
import com.zjcds.common.datasource.properties.MultipartDataSourceProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据源自动配置
 * 1、druid及hirika数据源自动配置
 * 2、多数据源支持
 * created date：2016-12-08
 * @author niezhegang
 */
@Configuration
@ConditionalOnClass({DataSource.class})
@EnableConfigurationProperties({MultipartDataSourceProperties.class,DruidStatProperties.class})
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Import(EmptyDataSourceRegister.class)
public class DsAutoConfiguration {
    private final static String DefaultDataSourceName = "dataSource";
    /**启用的数据源名称列表，用逗号分隔*/
    private final static String DataSourceNamesProperty = "com.zjcds.dataSources.names";
    /**启用多个数据源时指定的主数据源*/
    private final static String PrimaryDataSourceProperty = "com.zjcds.dataSources.primary";

    private static final Logger LOGGER = LoggerFactory.getLogger(DsAutoConfiguration.class);

    /**
     * 多数据源创建器
     */
    @Configuration
    public static class MultiDataSourceCreators implements InitializingBean{
        /**数据源属性配置*/
        @Autowired
        private MultipartDataSourceProperties dataSourceProperties;

        private List<DataSourceBeanCreator> dataSourceBeanCreators = new ArrayList<>();

        @Override
        public void afterPropertiesSet() throws Exception {
            dataSourceBeanCreators.add(new DruidDataSourceBeanCreator());
            dataSourceBeanCreators.add(new HikariDataSourceBeanCreator());
        }

        /**
         * Create data source data source.
         * @param applicationContext the application context
         * @param beanName           the bean name
         * @return the data source
         * @throws BeansException the beans exception
         */
        public DataSource createDataSource(GenericApplicationContext applicationContext,String beanName) {
            Assert.notNull(dataSourceProperties,"dataSourceProperties cannot be null");
            String dataSourceName = beanName;
            DataSourceBeanCreator dataSourceBeanCreator = null;
            for(DataSourceBeanCreator ele : dataSourceBeanCreators){
                if(ele.matchCreator(dataSourceProperties,dataSourceName)){
                    dataSourceBeanCreator = ele;
                    break;
                }
            }
            if(dataSourceBeanCreator == null){
                throw new IllegalArgumentException("未找到数据源"+dataSourceName+"的配置项");
            }
            else {
                return dataSourceBeanCreator.createDataSource(dataSourceProperties,applicationContext,dataSourceName);
            }

        }
    }

//    @Bean
//    @ConditionalOnMissingBean(DozerBeanMapper.class)
//    public DozerBeanMapper dozerBeanMapper(){
//        return new DozerBeanMapper();
//    }

    /**
     * 数据源配置BeanPostProcessor，替换占位的EmptyDataSource
     * niezhegang
     */
    @Configuration
    public static class DataSourceConfigurationPostProcessor implements BeanPostProcessor,ApplicationContextAware,PriorityOrdered{

        private ApplicationContext applicationContext;

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            Object retObj = bean;
            if(applicationContext instanceof GenericApplicationContext){
                /**处理逻辑*/
                if(bean instanceof EmptyDataSource){
                    MultiDataSourceCreators multiDataSourceCreators = applicationContext.getBean(MultiDataSourceCreators.class);
                   retObj = multiDataSourceCreators.createDataSource((GenericApplicationContext)applicationContext,beanName);
                }
            }
            return retObj;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

    public static interface DataSourceBeanCreator{

        /**
         * 查看是否匹配创建对象
         * @param dataSourceProperties
         * @param dataSourceName
         * @return
         */
        public boolean matchCreator(MultipartDataSourceProperties dataSourceProperties,String dataSourceName);

        /**
         * 创建类型对应数据源
         * @param applicationContext
         * @param dataSourceName
         * @return
         */
        public DataSource createDataSource(MultipartDataSourceProperties dataSourceProperties,GenericApplicationContext applicationContext,String dataSourceName);

    }

    public static class DruidDataSourceBeanCreator implements DataSourceBeanCreator {

        @Override
        public boolean matchCreator(MultipartDataSourceProperties dataSourceProperties, String dataSourceName) {
            boolean bRet = false;
            if(dataSourceProperties != null) {
                Map<String, DruidDataSourceProperties> druidDataSourceProperties = dataSourceProperties.getDruids();
                if (druidDataSourceProperties != null && druidDataSourceProperties.get(dataSourceName) != null) {
                    bRet = true;
                }
            }
            return bRet;
        }

        @Override
        public DataSource createDataSource(MultipartDataSourceProperties dataSourceProperties,GenericApplicationContext applicationContext, String dataSourceName) {
            Assert.notNull(dataSourceProperties,"dataSourceProperties cannot be null");
            DruidDataSourceProperties druidDataSourceProperties = dataSourceProperties.getDruids().get(dataSourceName);
            Assert.notNull(druidDataSourceProperties, dataSourceName +" druidConfig not found!");
            //修改默认druidDataSource的name为beanname
            if(StringUtils.isBlank(druidDataSourceProperties.getName())) {
                druidDataSourceProperties.setName(dataSourceName);
            }
            DataSource ds = DataSourceBuilder.newDruidBuilder()
                    .dataSourceProperties(druidDataSourceProperties)
                    .build();
            LOGGER.info("druid数据源{}的属性配置完成！", dataSourceName);
            return ds;
        }
    }

    public static class HikariDataSourceBeanCreator implements DataSourceBeanCreator {
        @Override
        public boolean matchCreator(MultipartDataSourceProperties dataSourceProperties, String dataSourceName) {
            boolean bRet = false;
            if(dataSourceProperties != null) {
                Map<String, HikariDataSourceProperties> hikariDataSourceProperties = dataSourceProperties.getHikaris();
                if (hikariDataSourceProperties != null && hikariDataSourceProperties.get(dataSourceName) != null) {
                    bRet = true;
                }
            }
            return bRet;
        }

        @Override
        public DataSource createDataSource(MultipartDataSourceProperties dataSourceProperties,GenericApplicationContext applicationContext, String dataSourceName) {
            Assert.notNull(dataSourceProperties,"dataSourceProperties cannot be null");
            HikariDataSourceProperties hikariDataSourceProperties = dataSourceProperties.getHikaris().get(dataSourceName);
            Assert.notNull(hikariDataSourceProperties, dataSourceName +" hikariDataSourceConfig not found!");
            //修改默认druidDataSource的name为beanname
            if(StringUtils.isBlank(hikariDataSourceProperties.getPoolName())) {
                hikariDataSourceProperties.setPoolName(dataSourceName);
            }
            DataSource ds = DataSourceBuilder.newHikariBuilder()
                    .hikariDataSourceProperties(hikariDataSourceProperties)
                    .build();
            LOGGER.info("hikari数据源{}的属性配置完成！", dataSourceName);
            return ds;
        }
    }



    @Configuration
    @ConditionalOnWebApplication
    public static class WebStatFilterConfiguration {
        @Bean
        public ServletRegistrationBean servletRegistrationBean(DruidStatProperties druidStatProperties){
            ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet());
            if(StringUtils.isNotBlank(druidStatProperties.getServlet().getUrl())){
                servletRegistrationBean.addUrlMappings(druidStatProperties.getServlet().getUrl());
            }
            //由权限框架做限制
//            if(StringUtils.isNotBlank(druidStatProperties.getServlet().getLoginUsername())
//                    && StringUtils.isNotBlank(druidStatProperties.getServlet().getLoginPassword())){
//                servletRegistrationBean.addInitParameter("loginUsername",druidStatProperties.getServlet().getLoginUsername());
//                servletRegistrationBean.addInitParameter("loginPassword",druidStatProperties.getServlet().getLoginPassword());
//            }
            return servletRegistrationBean;
        }

        @Bean
        public FilterRegistrationBean filterRegistrationBean(DruidStatProperties druidStatProperties){
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
            if(StringUtils.isNotBlank(druidStatProperties.getFilter().getUrl())){
                filterRegistrationBean.addUrlPatterns(druidStatProperties.getFilter().getUrl());
            }
            if(StringUtils.isNotBlank(druidStatProperties.getFilter().getExclusions())){
                filterRegistrationBean.addInitParameter("exclusions",druidStatProperties.getFilter().getExclusions());
            }
            return filterRegistrationBean;
        }
    }

}
