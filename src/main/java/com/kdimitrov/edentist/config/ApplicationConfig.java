package com.kdimitrov.edentist.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "edentist.app")
public class ApplicationConfig {

    private String superadminEmail;

    public String getSuperadminEmail() {
        return superadminEmail;
    }

    public void setSuperadminEmail(String superadminEmail) {
        this.superadminEmail = superadminEmail;
    }
}
