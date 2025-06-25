package com.example.demo.produtoVendas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProdutoVendaRepository extends JpaRepository<ProdutoVendasEntity, Long> {
    @Query("SELECT pv FROM ProdutoVendasEntity pv " +
           "WHERE (pv.venda.id = :mId)")
    List<ProdutoVendasEntity> getVendas(@Param("mId") Long mId);
}
