package com.cydercode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {

    @Value("${app.email-verification-url}")
    private String activationBaseUrl;

    @Value("${app.recovery-url}")
    private String recoveryUrl;

}
