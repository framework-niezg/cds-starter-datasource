package com.zjcds.common.datasource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * created date：2016-12-11
 *
 * @author niezhegang
 */
public class EmptyDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware,PriorityOrdered {
    /**启用的数据源名称列表，用逗号分隔*/
    private final static String DataSourceNamesProperty = "com.zjcds.dataSources.names";
    /**启用多个数据源时指定的主数据源*/
    private final static String PrimaryDataSourceProperty = "com.zjcds.dataSources.primary";
    /**日志记录*/
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**环境对象*/
    private Environment environment;


    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 10;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private String getPrimaryDataSource(String[] dataSourceArray){
        String primaryDataSource;
        //只配置了一个数据源，则将其设为主数据源
        if(dataSourceArray.length == 1){
            primaryDataSource = dataSourceArray[0];
        }
        else{
            primaryDataSource = StringUtils.trim(environment.getProperty(PrimaryDataSourceProperty));
            Assert.hasText(primaryDataSource,"配置了多个数据源，但是未指定主数据源配置("+PrimaryDataSourceProperty+")!");
            Assert.isTrue(!ArrayUtils.contains(dataSourceArray,primaryDataSource),"配置了多个数据源，但是指定的主数据源名称未匹配！");
        }
        return primaryDataSource;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        String dataSourceNames = StringUtils.trim(environment.getProperty(DataSourceNamesProperty));
        Assert.hasText(dataSourceNames,"未配置"+DataSourceNamesProperty+"属性");
        RootBeanDefinition rootBeanDefinition;
        String[] dataSourceArray = StringUtils.split(dataSourceNames,",");
        //获取主数据源
        String primaryDataSource = getPrimaryDataSource(dataSourceArray);
        for(String dataSourceName:dataSourceArray) {
            dataSourceName = StringUtils.trimToEmpty(dataSourceName);
            if (StringUtils.isBlank(dataSourceName))
                continue;
            //注册一个空数据源
            rootBeanDefinition = new RootBeanDefinition(EmptyDataSource.class);
            rootBeanDefinition.setAttribute("id", dataSourceName);
            rootBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
            rootBeanDefinition.setTargetType(DataSource.class);
            if (StringUtils.equals(dataSourceName, primaryDataSource)) {
                rootBeanDefinition.setPrimary(true);
            }
            registry.registerBeanDefinition(dataSourceName, rootBeanDefinition);
            logger.debug("注册{}数据源", dataSourceName);
        }
    }
}
