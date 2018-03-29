package com.zjcds.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zjcds.common.datasource.properties.DruidDataSourceProperties;
import com.zjcds.common.datasource.properties.HikariDataSourceProperties;
import lombok.Getter;
import org.dozer.DozerBeanMapper;
import org.springframework.util.Assert;

import javax.sql.DataSource;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * 创建数据源工厂类对象
 * created date：2018-03-28
 * @author niezhegang
 */
public final class DataSourceBuilder {
    private static DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
    /**
     * 创建一个druid数据源builder对象
     * @return
     */
    public static DruidDataSourceBuilder newDruidBuilder(){
        return new DruidDataSourceBuilder();
    }

    /**
     * 创建一个Hikari数据源builder对象
     * @return
     */
    public static HikariDataSourceBuilder newHikariBuilder(){
        return new HikariDataSourceBuilder();
    }

    @Getter
    public static class HikariDataSourceBuilder {

        private  long connectionTimeout = SECONDS.toMillis(30);
        private  long validationTimeout = SECONDS.toMillis(5);
        private  long idleTimeout = MINUTES.toMillis(10);
        private  long leakDetectionThreshold;
        private  long maxLifetime = MINUTES.toMillis(30);
        private  int maxPoolSize = 10;
        private  int minIdle = 10;
        private String dataSourceClassName;
        private String jdbcUrl;
        private  String username;
        private  String password;

        // Properties NOT changeable at runtime
        private long initializationFailTimeout = 1;
        private String catalog;
        private String connectionInitSql;
        private String connectionTestQuery;

        private String dataSourceJndiName;
        private String driverClassName;

        private String poolName;
        private String schema;
        private String transactionIsolationName;
        private boolean isAutoCommit = true;
        private boolean isReadOnly;
        private boolean isIsolateInternalQueries;
        private boolean isRegisterMbeans;
        private boolean isAllowPoolSuspension;

        private HikariDataSourceBuilder() {
        }

        public HikariDataSourceBuilder  hikariDataSourceProperties(HikariDataSourceProperties hikariDataSourceProperties) {
            if(hikariDataSourceProperties != null){
                connectionTimeout(hikariDataSourceProperties.getConnectionTimeout());
                validationTimeout(hikariDataSourceProperties.getValidationTimeout());
                idleTimeout(hikariDataSourceProperties.getIdleTimeout());
                leakDetectionThreshold(hikariDataSourceProperties.getLeakDetectionThreshold());
                maxLifetime(hikariDataSourceProperties.getMaxLifetime());
                maxPoolSize(hikariDataSourceProperties.getMaxPoolSize());
                minIdle(hikariDataSourceProperties.getMinIdle());
                dataSourceClassName(hikariDataSourceProperties.getDataSourceClassName());
                jdbcUrl(hikariDataSourceProperties.getJdbcUrl());
                username(hikariDataSourceProperties.getUsername());
                password(hikariDataSourceProperties.getPassword());
                initializationFailTimeout(hikariDataSourceProperties.getInitializationFailTimeout());
                catalog(hikariDataSourceProperties.getCatalog());
                connectionInitSql(hikariDataSourceProperties.getConnectionInitSql());
                connectionTestQuery(hikariDataSourceProperties.getConnectionTestQuery());
                dataSourceJndiName(hikariDataSourceProperties.getDataSourceJndiName());
                driverClassName(hikariDataSourceProperties.getDriverClassName());
                poolName(hikariDataSourceProperties.getPoolName());
                schema(hikariDataSourceProperties.getSchema());
                transactionIsolationName(hikariDataSourceProperties.getTransactionIsolationName());
                isAutoCommit(hikariDataSourceProperties.isAutoCommit());
                isReadOnly(hikariDataSourceProperties.isReadOnly());
                isIsolateInternalQueries(hikariDataSourceProperties.isIsolateInternalQueries());
                isRegisterMbeans(hikariDataSourceProperties.isRegisterMbeans());
                isAllowPoolSuspension(hikariDataSourceProperties.isAllowPoolSuspension());
            }
            return this;
        }

        public HikariDataSourceBuilder connectionTimeout(long val) {
            connectionTimeout = val;
            return this;
        }

        public HikariDataSourceBuilder validationTimeout(long val) {
            validationTimeout = val;
            return this;
        }

        public HikariDataSourceBuilder idleTimeout(long val) {
            idleTimeout = val;
            return this;
        }

        public HikariDataSourceBuilder leakDetectionThreshold(long val) {
            leakDetectionThreshold = val;
            return this;
        }

        public HikariDataSourceBuilder maxLifetime(long val) {
            maxLifetime = val;
            return this;
        }

        public HikariDataSourceBuilder maxPoolSize(int val) {
            maxPoolSize = val;
            return this;
        }

        public HikariDataSourceBuilder minIdle(int val) {
            minIdle = val;
            return this;
        }

        public HikariDataSourceBuilder dataSourceClassName(String val) {
            dataSourceClassName = val;
            return this;
        }

        public HikariDataSourceBuilder jdbcUrl(String val) {
            jdbcUrl = val;
            return this;
        }

        public HikariDataSourceBuilder username(String val) {
            username = val;
            return this;
        }

        public HikariDataSourceBuilder password(String val) {
            password = val;
            return this;
        }

        public HikariDataSourceBuilder initializationFailTimeout(long val) {
            initializationFailTimeout = val;
            return this;
        }

        public HikariDataSourceBuilder catalog(String val) {
            catalog = val;
            return this;
        }

        public HikariDataSourceBuilder connectionInitSql(String val) {
            connectionInitSql = val;
            return this;
        }

        public HikariDataSourceBuilder connectionTestQuery(String val) {
            connectionTestQuery = val;
            return this;
        }

        public HikariDataSourceBuilder dataSourceJndiName(String val) {
            dataSourceJndiName = val;
            return this;
        }

        public HikariDataSourceBuilder driverClassName(String val) {
            driverClassName = val;
            return this;
        }

        public HikariDataSourceBuilder poolName(String val) {
            poolName = val;
            return this;
        }

        public HikariDataSourceBuilder schema(String val) {
            schema = val;
            return this;
        }

        public HikariDataSourceBuilder transactionIsolationName(String val) {
            transactionIsolationName = val;
            return this;
        }

        public HikariDataSourceBuilder isAutoCommit(boolean val) {
            isAutoCommit = val;
            return this;
        }

        public HikariDataSourceBuilder isReadOnly(boolean val) {
            isReadOnly = val;
            return this;
        }

        public HikariDataSourceBuilder isIsolateInternalQueries(boolean val) {
            isIsolateInternalQueries = val;
            return this;
        }

        public HikariDataSourceBuilder isRegisterMbeans(boolean val) {
            isRegisterMbeans = val;
            return this;
        }

        public HikariDataSourceBuilder isAllowPoolSuspension(boolean val) {
            isAllowPoolSuspension = val;
            return this;
        }

        public HikariDataSource build() {
            HikariConfig config = new HikariConfig();
            dozerBeanMapper.map(this,config);
            return new HikariDataSource(config);
        }

    }


    @Getter
    public static class DruidDataSourceBuilder {
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

        private DruidDataSourceBuilder() {
        }

        public DruidDataSourceBuilder dataSourceProperties(DruidDataSourceProperties druidDataSourceProperties){
            if(druidDataSourceProperties != null) {
                name(druidDataSourceProperties.getName());
                driverClassName(druidDataSourceProperties.getDriverClassName());
                url(druidDataSourceProperties.getUrl());
                username(druidDataSourceProperties.getUsername());
                password(druidDataSourceProperties.getPassword());
                initialSize(druidDataSourceProperties.getInitialSize());
                maxActive(druidDataSourceProperties.getMaxActive());
                minIdle(druidDataSourceProperties.getMinIdle());
                maxIdle(druidDataSourceProperties.getMaxIdle());
                maxWait(druidDataSourceProperties.getMaxWait());
                validationQuery(druidDataSourceProperties.getValidationQuery());
                validationQueryTimeout(druidDataSourceProperties.getValidationQueryTimeout());
                testOnBorrow(druidDataSourceProperties.isTestOnBorrow());
                testOnReturn(druidDataSourceProperties.isTestOnReturn());
                testWhileIdle(druidDataSourceProperties.isTestWhileIdle());
                timeBetweenEvictionRunsMillis(druidDataSourceProperties.getTimeBetweenEvictionRunsMillis());
                minEvictableIdleTimeMillis(druidDataSourceProperties.getMinEvictableIdleTimeMillis());
                filters(druidDataSourceProperties.getFilters());
                useUnfairLock(druidDataSourceProperties.getUseUnfairLock());
                timeBetweenLogStatsMillis(druidDataSourceProperties.getTimeBetweenLogStatsMillis());
            }
            return this;
        }

        public DruidDataSourceBuilder name(String val) {
            name = val;
            return this;
        }

        public DruidDataSourceBuilder driverClassName(String val) {
            driverClassName = val;
            return this;
        }

        public DruidDataSourceBuilder url(String val) {
            url = val;
            return this;
        }

        public DruidDataSourceBuilder username(String val) {
            username = val;
            return this;
        }

        public DruidDataSourceBuilder password(String val) {
            password = val;
            return this;
        }

        public DruidDataSourceBuilder initialSize(int val) {
            initialSize = val;
            return this;
        }

        public DruidDataSourceBuilder maxActive(int val) {
            maxActive = val;
            return this;
        }

        public DruidDataSourceBuilder minIdle(int val) {
            minIdle = val;
            return this;
        }

        public DruidDataSourceBuilder maxIdle(int val) {
            maxIdle = val;
            return this;
        }

        public DruidDataSourceBuilder maxWait(long val) {
            maxWait = val;
            return this;
        }

        public DruidDataSourceBuilder validationQuery(String val) {
            validationQuery = val;
            return this;
        }

        public DruidDataSourceBuilder validationQueryTimeout(int val) {
            validationQueryTimeout = val;
            return this;
        }

        public DruidDataSourceBuilder testOnBorrow(boolean val) {
            testOnBorrow = val;
            return this;
        }

        public DruidDataSourceBuilder testOnReturn(boolean val) {
            testOnReturn = val;
            return this;
        }

        public DruidDataSourceBuilder testWhileIdle(boolean val) {
            testWhileIdle = val;
            return this;
        }

        public DruidDataSourceBuilder timeBetweenEvictionRunsMillis(long val) {
            timeBetweenEvictionRunsMillis = val;
            return this;
        }

        public DruidDataSourceBuilder minEvictableIdleTimeMillis(long val) {
            minEvictableIdleTimeMillis = val;
            return this;
        }

        public DruidDataSourceBuilder filters(String val) {
            filters = val;
            return this;
        }

        public DruidDataSourceBuilder useUnfairLock(Boolean val) {
            useUnfairLock = val;
            return this;
        }

        public DruidDataSourceBuilder timeBetweenLogStatsMillis(long val) {
            timeBetweenLogStatsMillis = val;
            return this;
        }

        public DataSource build(){
            Assert.hasText(url,"连接url不能为空！");
            Assert.hasText(driverClassName,"驱动类名不能为空！");
            Assert.hasText(username,"用户名不能为空！");
            DruidDataSource dataSource = new DruidDataSource();
            dozerBeanMapper.map(this,dataSource);
            return dataSource;
        }

    }

}
