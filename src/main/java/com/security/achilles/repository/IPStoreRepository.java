package com.security.achilles.repository;

import com.security.achilles.entity.IPStoreEntity;
import com.security.achilles.entity.VersionStoreEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPStoreRepository extends CrudRepository<IPStoreEntity, String> {
}
