package com.example.demo.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    ProdutoEntity findByNome(String nome);

    @Modifying
    @Transactional
    @Query("UPDATE ProdutoEntity p SET p.estoqueAtual = p.estoqueAtual + :mSaldo WHERE (p.id = :mId)")
    void ajustarSaldo(@Param("mSaldo") Float mSaldo, @Param("mId") Long mId);
}
