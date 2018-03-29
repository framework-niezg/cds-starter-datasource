package com.zjcds.common.datasource.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * 增加多数据源配置支持
 * created date：2016-12-04
 * @author niezhegang
 */
@ConfigurationProperties(prefix = "com.zjcds.dataSources",ignoreUnknownFields = true)
@SuppressWarnings("unused")
@Getter
@Setter
public class MultipartDataSourceProperties {

    /** key为数据源名称，value为数据源配置*/
    @NestedConfigurationProperty
    private Map<String,DruidDataSourceProperties> druids;
    @NestedConfigurationProperty
    private Map<String,HikariDataSourceProperties> hikaris;


}
