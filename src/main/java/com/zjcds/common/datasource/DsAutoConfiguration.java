package com.zjcds.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
import java.util.Map;

/**
 * 数据源自动配置
 * 1、druid数据源自动配置
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

    /**
     * The type Druid data source configuration.
     * niezhegang
     */
    @Configuration
    @ConditionalOnClass(DruidDataSource.class)
    public static class DruidDataSourceConfiguration {
        /**数据源属性配置*/
        @Autowired
        private MultipartDataSourceProperties dataSourceProperties;

        private DozerBeanMapper mapper = new DozerBeanMapper();

        private Logger logger = LoggerFactory.getLogger(this.getClass());

        /**
         * check data source config existence
         * @param dataSourceName the data source name
         * @return the boolean
         */
        public boolean existDataSourceConfig(String dataSourceName){
            boolean bRet = false;
            Map<String, MultipartDataSourceProperties.DruidDataSourceProperties> druidDataSourceProperties = dataSourceProperties.getDruids();
            if(druidDataSourceProperties != null && druidDataSourceProperties.get(dataSourceName) != null){
                bRet = true;
            }
            return bRet;
        }

        /**
         * Create data source data source.
         * @param applicationContext the application context
         * @param beanName           the bean name
         * @return the data source
         * @throws BeansException the beans exception
         */
        public DataSource createDataSource(GenericApplicationContext applicationContext,String beanName) throws BeansException {
            Assert.notNull(dataSourceProperties,"dataSourceProperties cannot be null");
            DataSource ds = new DruidDataSource();
            MultipartDataSourceProperties.DruidDataSourceProperties druidDataSourceProperties = dataSourceProperties.getDruids().get(beanName);
            Assert.notNull(druidDataSourceProperties,beanName+" druidConfig not found!");
            //修改默认druidDataSource的name为beanname
            if(StringUtils.isBlank(druidDataSourceProperties.getName())) {
                druidDataSourceProperties.setName(beanName);
            }
            mapper.map(druidDataSourceProperties,ds);
            logger.debug("数据源{}的属性配置完成！",beanName);
            return ds;
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
                    DruidDataSourceConfiguration druidDataSourceConfiguration = applicationContext.getBean(DruidDataSourceConfiguration.class);
                    if(druidDataSourceConfiguration != null){
                        if(druidDataSourceConfiguration.existDataSourceConfig(beanName)){
                            retObj = druidDataSourceConfiguration.createDataSource((GenericApplicationContext)applicationContext,beanName);
                        }
                    }
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
