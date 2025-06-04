package com.example.demo.produtoCompras;

import com.example.demo.enums.StatusCompraEnum;
import com.example.demo.enums.StatusVendaEnum;
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

    @JsonProperty("status")
    private String status = String.valueOf(StatusCompraEnum.PENDENTE);

    public CompraRequestDTO() {}

    public CompraRequestDTO(Long id_cliente, LocalDate data_compra, BigDecimal total, List<ProdutoCompraDTO> lista_produto,
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProdutoCompraDTO> getLista_produto() {
        return lista_produto;
    }

    public void setLista_produto(List<ProdutoCompraDTO> lista_produto) {
        this.lista_produto = lista_produto;
    }
}
