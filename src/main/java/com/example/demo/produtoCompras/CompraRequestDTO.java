package com.example.demo.produtoCompras;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CompraRequestDTO {
    @JsonProperty("id_cliente")
    @NotNull(message = "Campo 'id_cliente' não preenchido no body, verifique.")
    private Long id_cliente;

    @JsonProperty("lista_produto")
    @NotNull(message = "Campo 'lista_produto' não preenchido no body, verifique")
    private List<ProdutoCompraDTO> lista_produto;

    public CompraRequestDTO() {}

    public CompraRequestDTO(Long id_cliente, LocalDate data_compra, BigDecimal total, List<ProdutoCompraDTO> lista_produto) {
        this.id_cliente = id_cliente;
        this.lista_produto = lista_produto;
    }

    // Getters e Setters

    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
    }

    public List<ProdutoCompraDTO> getLista_produto() {
        return lista_produto;
    }

    public void setLista_produto(List<ProdutoCompraDTO> lista_produto) {
        this.lista_produto = lista_produto;
    }
}
