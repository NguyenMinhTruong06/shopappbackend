package com.project.productservice.configurations.cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        String CLOUD_NAME = "dyvcb0odq";
        config.put("cloud_name", CLOUD_NAME);
        String API_KEY = "541844322436457";
        config.put("api_key", API_KEY);
        String API_SECRET = "fHAgXDoYcITe5bpHlJMyK3Pwjl4";
        config.put("api_secret", API_SECRET);

        return new Cloudinary(config);
    }
}