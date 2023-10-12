package com.security.achilles.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@Configuration
public class VersionKey {
    private Map<String, String> memory = new HashMap<>();
}
