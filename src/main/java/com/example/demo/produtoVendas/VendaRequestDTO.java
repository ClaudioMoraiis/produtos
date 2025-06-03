package com.example.demo.produtoVendas;

import com.example.demo.enums.StatusVendaEnum;
import com.example.demo.produtoCompras.ProdutoCompraDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class VendaRequestDTO {
    @JsonProperty("id_cliente")
    @NotNull(message = "Campo 'id_cliente' não preenchido no body, verifique.")
    private Long id_cliente;

    @JsonProperty("lista_produto")
    @NotNull(message = "Campo 'lista_produto' não preenchido no body, verifique")
    private List<ProdutoVendasDTO> lista_produto;

    @JsonProperty("status")
    private String status = String.valueOf(StatusVendaEnum.PENDENTE);

    public VendaRequestDTO() {}

    public VendaRequestDTO(Long id_cliente, LocalDate data_compra, BigDecimal total, List<ProdutoVendasDTO> lista_produto,
                           String status) {
        this.id_cliente = id_cliente;
        this.lista_produto = lista_produto;
        this.status = status;
    }

    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public List<ProdutoVendasDTO> getLista_produto() {
        return lista_produto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLista_produto(List<ProdutoVendasDTO> lista_produto) {
        this.lista_produto = lista_produto;
    }
}
