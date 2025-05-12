package com.example.demo.repository;

import com.example.demo.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UsuarioEntity, Long> {
    UsuarioEntity findByEmail(String mEmail);

    boolean existsByEmail(String email);
}
