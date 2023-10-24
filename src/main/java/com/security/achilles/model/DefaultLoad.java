package com.security.achilles.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "default-load")
public class DefaultLoad {
    private String versionId;
    private String decoder;
}
