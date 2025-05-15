package com.example.demo.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UsuarioEntity, Long> {
    UsuarioEntity findByEmail(String mEmail);

    boolean existsByEmail(String email);
}
