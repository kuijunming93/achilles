package com.security.achilles.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class Broadcast {
    private List<Map<String, String>> content = List.of(
            Map.of("V1.1.3", "- Testing...")
    );
}
