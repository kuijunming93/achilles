package com.security.achilles.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppConfiguration {

    @Value("${spring.user.secret}")
    private String SECRET;
    @Value("${spring.admin.secret}")
    private String ADMIN_SECRET;


    public String getSECRET() {
        return SECRET;
    }

    public String getADMIN_SECRET() {
        return ADMIN_SECRET;
    }

}
