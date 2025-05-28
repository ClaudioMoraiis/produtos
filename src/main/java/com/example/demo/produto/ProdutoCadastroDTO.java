package com.example.demo.produto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoCadastroDTO {

    @JsonProperty("nome")
    @NotNull(message = "Obrigatório preenchimeto do campo  'nome' no body")
    private String nome;

    @JsonProperty("preco_custo")
    private BigDecimal precoCusto;

    @JsonProperty("preco_venda")
    private BigDecimal precoVenda;

    @JsonProperty("estoque_atual")
    private Float estoqueAtual;

    @JsonProperty("unidade_medida")
    @NotNull(message = "Obrigatório preenchimeto do campo 'unidade_medida' no body")
    private String unidadeMedida;

    @JsonProperty("ativo")
    @NotNull(message = "Obrigatório preenchimeto do campo 'ativo' no body")
    private Boolean ativo;

    public ProdutoCadastroDTO(String nome, BigDecimal precoCusto, BigDecimal precoVenda, Float estoqueAtual, String unidadeMedida, Boolean ativo) {
        this.nome = nome;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.estoqueAtual = estoqueAtual;
        this.unidadeMedida = unidadeMedida;
        this.ativo = ativo;
    }

    public ProdutoCadastroDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Float getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(Float estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProdutoCadastroDTO that)) return false;

        return nome.equals(that.nome) && precoCusto.equals(that.precoCusto) && precoVenda.equals(that.precoVenda) && estoqueAtual.equals(that.estoqueAtual) && unidadeMedida.equals(that.unidadeMedida) && ativo.equals(that.ativo);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + precoCusto.hashCode();
        result = 31 * result + precoVenda.hashCode();
        result = 31 * result + estoqueAtual.hashCode();
        result = 31 * result + unidadeMedida.hashCode();
        result = 31 * result + ativo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProdutoCadastroDTO{" +
                "nome='" + nome + '\'' +
                ", precoCusto=" + precoCusto +
                ", precoVenda=" + precoVenda +
                ", estoqueAtual=" + estoqueAtual +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
