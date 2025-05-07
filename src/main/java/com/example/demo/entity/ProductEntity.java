package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PRODUTO")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_id")
    private Long id;

    @Column(name = "pro_nome")
    private String nome;

    @Column(name = "pro_preco_custo")
    private BigDecimal precoCusto;

    @Column(name = "pro_preco_venda")
    private BigDecimal precoVenda;

    @Column(name = "pro_estoque_atual")
    private Integer estoqueAtual;

    @Column(name = "pro_unidade_medida")
    private String unidadeMedida;

    @Column(name = "pro_ativo")
    private Boolean ativo;

    @Column(name = "pro_data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "pro_data_utl_atualizacao")
    private LocalDate dataUltimaAtualizacao;

    public ProductEntity(Long id, String nome, BigDecimal precoCusto, BigDecimal precoVenda, Integer estoqueAtual,
      String unidadeMedida, Boolean ativo, LocalDate dataCadastro, LocalDate dataUltimaAtualizacao) {
        this.id = id;
        this.nome = nome;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.estoqueAtual = estoqueAtual;
        this.unidadeMedida = unidadeMedida;
        this.ativo = ativo;
        this.dataCadastro = dataCadastro;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public ProductEntity(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(Integer estoqueAtual) {
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

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDate getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDate dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProductEntity that)) return false;

        return id.equals(that.id) && nome.equals(that.nome) && precoCusto.equals(that.precoCusto) &&
               precoVenda.equals(that.precoVenda) && estoqueAtual.equals(that.estoqueAtual) &&
               unidadeMedida.equals(that.unidadeMedida) && ativo.equals(that.ativo) && dataCadastro.equals(that.dataCadastro) &&
               dataUltimaAtualizacao.equals(that.dataUltimaAtualizacao);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + precoCusto.hashCode();
        result = 31 * result + precoVenda.hashCode();
        result = 31 * result + estoqueAtual.hashCode();
        result = 31 * result + unidadeMedida.hashCode();
        result = 31 * result + ativo.hashCode();
        result = 31 * result + dataCadastro.hashCode();
        result = 31 * result + dataUltimaAtualizacao.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", precoCusto=" + precoCusto +
                ", precoVenda=" + precoVenda +
                ", estoqueAtual=" + estoqueAtual +
                ", unidadeMedida='" + unidadeMedida + '\'' +
                ", ativo=" + ativo +
                ", dataCadastro=" + dataCadastro +
                ", dataUltimaAtualizacao=" + dataUltimaAtualizacao +
                '}';
    }
}
