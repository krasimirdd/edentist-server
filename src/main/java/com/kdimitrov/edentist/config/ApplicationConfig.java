package com.kdimitrov.edentist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "edentist.app")
public class ApplicationConfig {

    private String superadminEmail;

    private NewsApiClientConfig newsApiClient;

    public String getSuperadminEmail() {
        return superadminEmail;
    }

    public void setSuperadminEmail(String superadminEmail) {
        this.superadminEmail = superadminEmail;
    }

    public NewsApiClientConfig getNewsApiClient() {
        return newsApiClient;
    }

    public void setNewsApiClient(NewsApiClientConfig newsApiClient) {
        this.newsApiClient = newsApiClient;
    }

    @Configuration
    @ConfigurationProperties(prefix = "edentist.app.newsapiclient")
    public class NewsApiClientConfig {
        private String token;
        private int limit;
        private int timeout;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }
    }
}
