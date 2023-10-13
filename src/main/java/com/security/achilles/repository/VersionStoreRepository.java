package com.security.achilles.repository;

import com.security.achilles.entity.VersionStoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionStoreRepository extends CrudRepository<VersionStoreEntity, String> {
}
