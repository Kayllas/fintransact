package com.fintransact.core.repository;

import com.fintransact.core.model.PixKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PixKeyRepository extends JpaRepository<PixKey, Long> {
    Optional<PixKey> findByKey(String key);

    List<PixKey> findByAccountId(Long accountId);

    boolean existsByKey(String key);
}
