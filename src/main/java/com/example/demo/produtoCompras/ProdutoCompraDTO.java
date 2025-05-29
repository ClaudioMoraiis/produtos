package com.example.demo.produtoCompras;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoCompraDTO {
    @JsonProperty("id_produto")
    @NotNull(message = "Campo 'id_produto' não informado no body, verifique")
    private Long id_produto;

    @JsonProperty("quantidade")
    @NotNull(message = "Campo 'quantidade' não informado no body, verifique")
    private Float quantidade;

    @JsonProperty("preco_unitario")
    @NotNull(message = "Campo 'preco_unitario' não informado no body, verifique")
    private BigDecimal preco_unitario;

    public ProdutoCompraDTO(){}

    public ProdutoCompraDTO(Long id_produto, Float quantidade, BigDecimal preco_unitario) {
        this.id_produto = id_produto;
        this.quantidade = quantidade;
        this.preco_unitario = preco_unitario;
    }

    public Long getId_produto() {
        return id_produto;
    }

    public void setId_produto(Long id_produto) {
        this.id_produto = id_produto;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco_unitario() {
        return preco_unitario;
    }

    public void setPreco_unitario(BigDecimal preco_unitario) {
        this.preco_unitario = preco_unitario;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProdutoCompraDTO that)) return false;

        return id_produto.equals(that.id_produto) && quantidade.equals(that.quantidade) && preco_unitario.equals(that.preco_unitario);
    }

    @Override
    public int hashCode() {
        int result = id_produto.hashCode();
        result = 31 * result + quantidade.hashCode();
        result = 31 * result + preco_unitario.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProdutoCompraDTO{" +
                "id_produto=" + id_produto +
                ", quantidade=" + quantidade +
                ", preco_unitario=" + preco_unitario +
                '}';
    }
}
