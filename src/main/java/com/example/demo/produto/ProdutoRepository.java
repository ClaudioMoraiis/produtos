package com.example.demo.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    ProdutoEntity findFirstByNomeAndAtivoTrue(String nome);

    @Modifying
    @Transactional
    @Query("UPDATE ProdutoEntity p SET p.estoqueAtual = p.estoqueAtual + :mSaldo WHERE (p.id = :mId)")
    void ajustarSaldo(@Param("mSaldo") Float mSaldo, @Param("mId") Long mId);

    @Modifying
    @Transactional
    @Query("UPDATE ProdutoEntity p SET p.precoCusto = :mPrecoCusto WHERE (p.id = :mId)")
    void ajustarPrecoCusto(@Param("mPrecoCusto") BigDecimal mPrecoCusto, @Param("mId") Long mId);

    @Modifying
    @Transactional
    @Query("UPDATE ProdutoEntity p SET p.estoqueAtual = p.estoqueAtual - :mQtdVenda WHERE (p.id = :mId)")
    void diminuirSaldo(@Param("mQtdVenda") Float mQtdVenda, @Param("mId") Long mId);

    @Modifying
    @Transactional
    @Query("UPDATE ProdutoEntity p SET p.precoVenda = :mPrecoVenda WHERE (p.id = :mId)")
    void ajustarPrecoVenda(@Param("mPrecoVenda") BigDecimal mPrecoVenda, @Param("mId") Long mId);

    @Modifying
    @Transactional
    @Query("UPDATE ProdutoEntity p SET p.ativo = false WHERE (p.id = :mId)")
    void inativar(@Param("mId") Long mId);
}
