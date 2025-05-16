package com.example.demo.produto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    ProdutoEntity findByNome(String nome);
}
