package com.example.demo.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    ClienteEntity findFirstByDocumentoOrderByIdAsc(String mDocumento);

    List<ClienteEntity> findAll();
}
