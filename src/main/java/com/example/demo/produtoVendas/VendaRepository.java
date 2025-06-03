package com.example.demo.produtoVendas;

import com.example.demo.venda.VendasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepository extends JpaRepository<VendasEntity, Long> {
}
