package com.example.demo.produtoMovimentacao;

import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.produto.ProdutoEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "PRODUTO_MOVIMENTACAO")
public class ProdutoMovimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prm_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prm_pro_id", referencedColumnName = "pro_id", nullable = false)
    private ProdutoEntity produto;

    @Column(name = "prm_tipo_movimento")
    private TipoMovimentoEnum tipoMovimento;

    @Column(name = "prm_quantidade")
    private Float quantidade;

    @Column(name = "prm_origem")
    private OrigemMovimentoEnum origemMovimentoEnum;

    @Column(name = "prm_origem_id")
    private Long idOrigem;

    @Column(name = "prm_data_movimento")
    private LocalDate dataMovimento;

    public ProdutoMovimentacao(Long id, ProdutoEntity produto, TipoMovimentoEnum tipoMovimento, Float quantidade,
                               OrigemMovimentoEnum origemMovimentoEnum, Long idOrigem, LocalDate dataMovimento) {
        this.id = id;
        this.produto = produto;
        this.tipoMovimento = tipoMovimento;
        this.quantidade = quantidade;
        this.origemMovimentoEnum = origemMovimentoEnum;
        this.idOrigem = idOrigem;
        this.dataMovimento = dataMovimento;
    }

    public ProdutoMovimentacao(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProdutoEntity getProduto() {
        return produto;
    }

    public void setProduto(ProdutoEntity produto) {
        this.produto = produto;
    }

    public TipoMovimentoEnum getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimentoEnum tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public OrigemMovimentoEnum getOrigemMovimentoEnum() {
        return origemMovimentoEnum;
    }

    public void setOrigemMovimentoEnum(OrigemMovimentoEnum origemMovimentoEnum) {
        this.origemMovimentoEnum = origemMovimentoEnum;
    }

    public Long getIdOrigem() {
        return idOrigem;
    }

    public void setIdOrigem(Long idOrigem) {
        this.idOrigem = idOrigem;
    }

    public LocalDate getDataMovimento() {
        return dataMovimento;
    }

    public void setDataMovimento(LocalDate dataMovimento) {
        this.dataMovimento = dataMovimento;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProdutoMovimentacao that)) return false;

        return produto.equals(that.produto) && tipoMovimento == that.tipoMovimento && quantidade.equals(that.quantidade) && origemMovimentoEnum == that.origemMovimentoEnum && idOrigem.equals(that.idOrigem) && dataMovimento.equals(that.dataMovimento);
    }

    @Override
    public int hashCode() {
        int result = produto.hashCode();
        result = 31 * result + tipoMovimento.hashCode();
        result = 31 * result + quantidade.hashCode();
        result = 31 * result + origemMovimentoEnum.hashCode();
        result = 31 * result + idOrigem.hashCode();
        result = 31 * result + dataMovimento.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProdutoMovimentacao{" +
                "id=" + id +
                ", produto=" + produto +
                ", tipoMovimento=" + tipoMovimento +
                ", quantidade=" + quantidade +
                ", origemMovimentoEnum=" + origemMovimentoEnum +
                ", idOrigem=" + idOrigem +
                ", dataMovimento=" + dataMovimento +
                '}';
    }
}
