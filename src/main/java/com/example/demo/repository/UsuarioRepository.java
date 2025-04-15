package com.example.demo.repository;

import com.example.demo.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository  extends JpaRepository<Usuario, Long> {
    UserDetails findByEmail(String mEmail);
}
