package com.example.demo.produto;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoAlterarDTO {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("unidade_medida")
    private String unidadeMedida;

    @JsonProperty("ativo")
    private Boolean ativo;

    public ProdutoAlterarDTO(String nome, String unidadeMedida, Boolean ativo) {
        this.nome = nome;
        this.unidadeMedida = unidadeMedida;
        this.ativo = ativo;
    }

    public ProdutoAlterarDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        if (!(o instanceof ProdutoAlterarDTO that)) return false;

        return nome.equals(that.nome) && unidadeMedida.equals(that.unidadeMedida) && ativo.equals(that.ativo);
    }

    @Override
    public int hashCode() {
        int result = nome.hashCode();
        result = 31 * result + unidadeMedida.hashCode();
        result = 31 * result + ativo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProdutoCadastroDTO{" +
                "nome='" + nome + '\'' +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
