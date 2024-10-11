package com.smartgate.main;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import okhttp3.OkHttpClient;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().build();
    }
}
