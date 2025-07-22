package com.example.demo.compra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CompraRepository extends JpaRepository<CompraEntity, Long> {
    @Query("SELECT c FROM CompraEntity c WHERE (c.data BETWEEN :mDataInicial AND :mDataFinal)")
    List<CompraEntity> buscarPorData(@Param("mDataInicial") LocalDate mDataInicial, @Param("mDataFinal") LocalDate mDataFinal);
}
