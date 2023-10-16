package com.security.achilles.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminRequest {
    private String versionId;
    private String decoder;
    private String userIp;
}
