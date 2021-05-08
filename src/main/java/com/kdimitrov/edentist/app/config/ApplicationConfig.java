package com.kdimitrov.edentist.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static com.kdimitrov.edentist.server.common.utils.Routes.AUTHORIZATION;
import static com.kdimitrov.edentist.server.common.utils.Routes.CODE_HEADER;
import static com.kdimitrov.edentist.server.common.utils.Routes.FILTER_HEADER;

@Configuration
@ConfigurationProperties(prefix = "edentist.app")
public class ApplicationConfig {

    private String superadminEmail;

    private NewsApiClientConfig newsApiClient;

    private String secret;

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

    public byte[] getSecret() {
        return secret.getBytes();
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
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

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
