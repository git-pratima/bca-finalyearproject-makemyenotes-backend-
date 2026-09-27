package com.pratima.makemyenotes.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dqkvjft06");
        config.put("api_key", "135533462222336");
        config.put("api_secret", "9XQsNoum-VaiWvev-8MaoCVwuHA");
        return new Cloudinary(config);
    }
}

