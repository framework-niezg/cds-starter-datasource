package com.zjcds.common.datasource.properties;

import javax.sql.DataSource;

/**
 * The type Durid data source properties.
 * niezhegang
 */
@SuppressWarnings("unused")
public class DruidDataSourceProperties {
    /**druid数据源类型*/
    public final static Class<? extends DataSource> DefaultType = com.alibaba.druid.pool.DruidDataSource.class;
    /**数据源类型*/
    private Class<? extends DataSource> type = DefaultType;
    /**配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。*/
    private String name;
    /**驱动类*/
    private String driverClassName;
    /**连接数据库的url，不同数据库不一样。*/
    private String url;
    /**连接数据库的用户名*/
    private String username;
    /**连接数据库的密码。如果你不希望密码直接写在配置文件中，可以使用ConfigFilter*/
    private String password;
    /**初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时*/
    private int initialSize = 0;
    /**最大连接池数量*/
    private int maxActive = 20;
    /**最小连接池数量*/
    private int minIdle = 20;
    /**最大连接池数量，druid中无效*/
    private int maxIdle;
    /**获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁*/
    private long maxWait = -1;
    /**用来检测连接是否有效的sql，要求是一个查询语句。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。*/
    private String validationQuery;
    /**单位：秒，检测连接是否有效的超时时间。底层调用jdbc Statement对象的void setQueryTimeout(int seconds)方法*/
    private int validationQueryTimeout = -1;
    /**申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能*/
    private boolean testOnBorrow = true;
    /**归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能*/
    private boolean testOnReturn = false;
    /**建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。*/
    private boolean testWhileIdle = false;
    /**有两个含义：1) Destroy线程会检测连接的间隔时间，如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接 2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明*/
    private long timeBetweenEvictionRunsMillis = 60 * 1000L;
    /**连接保持空闲而不被驱逐的最长时间*/
    private long minEvictableIdleTimeMillis = 1000L * 60L * 30L;
    /**属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有:监控统计用的filter:stat 日志用的filter:log4j 防御sql注入的filter:wall*/
    private String filters;
    /**使用非公平锁*/
    private Boolean useUnfairLock;
    /**默认配置每5分钟输出一次统计日志*/
    private long timeBetweenLogStatsMillis = 300000;

    public DruidDataSourceProperties() {

    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public int getValidationQueryTimeout() {
        return validationQueryTimeout;
    }

    public void setValidationQueryTimeout(int validationQueryTimeout) {
        this.validationQueryTimeout = validationQueryTimeout;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public Boolean getUseUnfairLock() {
        return useUnfairLock;
    }

    public void setUseUnfairLock(Boolean useUnfairLock) {
        this.useUnfairLock = useUnfairLock;
    }

    public Class<? extends DataSource> getType() {
        return type;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public long getTimeBetweenLogStatsMillis() {
        return timeBetweenLogStatsMillis;
    }

    public void setTimeBetweenLogStatsMillis(long timeBetweenLogStatsMillis) {
        this.timeBetweenLogStatsMillis = timeBetweenLogStatsMillis;
    }
}
