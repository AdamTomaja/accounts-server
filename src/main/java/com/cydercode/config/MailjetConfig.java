package com.cydercode.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class MailjetConfig {

  @Value("${app.mailjet.apiKey}")
  private String apiKey;

  @Value("${app.mailjet.secretKey}")
  private String secretKey;

  @Value("${app.mailjet.fromEmail}")
  private String fromEmail;

  @Value("${app.mailjet.fromName}")
  private String fromName;
}
