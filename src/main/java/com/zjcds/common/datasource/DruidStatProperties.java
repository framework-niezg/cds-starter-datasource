package com.zjcds.common.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * created date：2016-12-13
 * @author niezhegang
 */
@ConfigurationProperties(prefix = "com.zjcds.dataSources.druidStat",ignoreUnknownFields = true)
public class DruidStatProperties {
    private StatServletProperties servlet = new StatServletProperties();

    private StatFilterProperties filter = new StatFilterProperties();


    public StatServletProperties getServlet() {
        return servlet;
    }

    public void setServlet(StatServletProperties servlet) {
        this.servlet = servlet;
    }

    public StatFilterProperties getFilter() {
        return filter;
    }

    public void setFilter(StatFilterProperties filter) {
        this.filter = filter;
    }

    /**
     * The type Stat servlet properties.
     * niezhegang
     */
    public static class StatServletProperties {
        private String url = "/druid/*";
        private String loginUsername = "root";
        private String loginPassword = "root";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLoginUsername() {
            return loginUsername;
        }

        public void setLoginUsername(String loginUsername) {
            this.loginUsername = loginUsername;
        }

        public String getLoginPassword() {
            return loginPassword;
        }

        public void setLoginPassword(String loginPassword) {
            this.loginPassword = loginPassword;
        }
    }

    /**
     * The type Stat filter properties.
     * niezhegang
     */
    public static class StatFilterProperties {
        private String url = "/*";

        private String exclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getExclusions() {
            return exclusions;
        }

        public void setExclusions(String exclusions) {
            this.exclusions = exclusions;
        }
    }

}
