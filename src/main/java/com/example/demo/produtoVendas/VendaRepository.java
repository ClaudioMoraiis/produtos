package com.example.demo.produtoVendas;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.venda.VendasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VendaRepository extends JpaRepository<VendasEntity, Long> {
    @Query("SELECT v FROM VendasEntity v WHERE v.cliente.id = :mId")
    List<VendasEntity> listarPorCliente(@Param("mId") Long mId);

}
