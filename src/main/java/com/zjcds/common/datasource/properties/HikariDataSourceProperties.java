package com.zjcds.common.datasource.properties;

import lombok.Getter;
import lombok.Setter;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * created date：2018-03-29
 * @author niezhegang
 */
@Getter
@Setter
public class HikariDataSourceProperties {

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

}
