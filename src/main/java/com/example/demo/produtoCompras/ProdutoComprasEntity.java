package com.example.demo.produtoCompras;

import com.example.demo.compra.CompraEntity;
import com.example.demo.produto.ProdutoEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUTO_COMPRA")
public class ProdutoComprasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pco_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pco_com_id", referencedColumnName = "com_id", nullable = false)
    private CompraEntity compra;

    @ManyToOne
    @JoinColumn(name = "pco_pro_id", referencedColumnName = "pro_id", nullable = false)
    private ProdutoEntity produto;

    @Column(name = "pco_quantidade")
    private Float quantidade;

    @Column(name = "pco_preco_unitario")
    private BigDecimal precoUnitario;

    @Column(name = "pco_sub_total")
    private BigDecimal subTotal;

    public ProdutoComprasEntity(Long id, CompraEntity compra, ProdutoEntity produto, Float quantidade, BigDecimal precoUnitario, BigDecimal subTotal) {
        this.id = id;
        this.compra = compra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subTotal = subTotal;
    }

    public ProdutoComprasEntity(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompraEntity getCompra() {
        return compra;
    }

    public void setCompra(CompraEntity compra) {
        this.compra = compra;
    }

    public ProdutoEntity getProduto() {
        return produto;
    }

    public void setProduto(ProdutoEntity produto) {
        this.produto = produto;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ProdutoComprasEntity that)) return false;

        return compra.equals(that.compra) && produto.equals(that.produto) && quantidade.equals(that.quantidade) && precoUnitario.equals(that.precoUnitario) && subTotal.equals(that.subTotal);
    }

    @Override
    public int hashCode() {
        int result = compra.hashCode();
        result = 31 * result + produto.hashCode();
        result = 31 * result + quantidade.hashCode();
        result = 31 * result + precoUnitario.hashCode();
        result = 31 * result + subTotal.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProdutoComprasEntity{" +
                "id=" + id +
                ", compra=" + compra +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", subTotal=" + subTotal +
                '}';
    }
}

