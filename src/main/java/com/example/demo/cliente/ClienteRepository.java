package com.example.demo.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    ClienteEntity findFirstByDocumentoOrderByIdAsc(String mDocumento);

    List<ClienteEntity> findAll();

    Optional<ClienteEntity> findByEmail(String mEmail);

    @Query("SELECT COUNT(c) > 0 FROM ClienteEntity c WHERE (c.email = :mEmail)")
    Boolean getEmail(@Param("mEmail") String mEmail);
}
