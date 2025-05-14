package com.example.demo.repository;

import com.example.demo.entity.ProdutoEntity;
import com.example.demo.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

}
