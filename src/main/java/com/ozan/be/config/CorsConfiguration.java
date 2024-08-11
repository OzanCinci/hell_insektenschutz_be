package com.ozan.be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

  @Value("${application.cors.allowed-origins}")
  private String allowedOrigins;

  @Value("${application.cors.allowed-methods}")
  private String[] allowedMethods;

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        if ("*".equals(allowedOrigins)) {
          registry
              .addMapping("/api/**")
              .allowedOrigins("*")
              .allowedMethods(allowedMethods)
              .allowedHeaders("*");
        } else {
          registry
              .addMapping("/api/**")
              .allowedOrigins(allowedOrigins.split(","))
              .allowedMethods(allowedMethods)
              .allowedHeaders("*");
        }
      }
    };
  }
}
