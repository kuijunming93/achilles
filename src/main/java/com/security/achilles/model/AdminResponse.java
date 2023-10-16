package com.security.achilles.model;

import com.security.achilles.entity.IPStoreEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {
    List<IPStoreEntity> addresses;
    Integer total;
}
