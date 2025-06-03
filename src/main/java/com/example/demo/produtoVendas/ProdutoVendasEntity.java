package com.example.demo.produtoVendas;

import com.example.demo.venda.VendasEntity;
import com.example.demo.produto.ProdutoEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUTO_VENDAS")
public class ProdutoVendasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pve_ven_id", referencedColumnName = "ven_id", nullable = false)
    private VendasEntity venda;

    @ManyToOne
    @JoinColumn(name = "pve_pro_id", referencedColumnName = "pro_id", nullable = false)
    private ProdutoEntity produto;

    @Column(name = "pve_quantidade")
    private Float quantidade;

    @Column(name = "pve_preco_unitario")
    private BigDecimal precoUnitario;

    @Column(name = "pve_sub_total")
    private BigDecimal subTotal; // quantidade * preco unitario

    public ProdutoVendasEntity(Long id, VendasEntity venda, ProdutoEntity produto, Float quantidade, BigDecimal precoUnitario, BigDecimal subTotal) {
        this.id = id;
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subTotal = subTotal;
    }

    public ProdutoVendasEntity(VendasEntity venda, ProdutoEntity produto, Float quantidade, BigDecimal precoUnitario, BigDecimal subTotal) {
        this.venda = venda;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subTotal = subTotal;
    }

    public ProdutoVendasEntity(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VendasEntity getVenda() {
        return venda;
    }

    public void setVenda(VendasEntity venda) {
        this.venda = venda;
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
        if (!(o instanceof ProdutoVendasEntity that)) return false;

        return venda.equals(that.venda) && produto.equals(that.produto) && quantidade.equals(that.quantidade) && precoUnitario.equals(that.precoUnitario) && subTotal.equals(that.subTotal);
    }

    @Override
    public int hashCode() {
        int result = venda.hashCode();
        result = 31 * result + produto.hashCode();
        result = 31 * result + quantidade.hashCode();
        result = 31 * result + precoUnitario.hashCode();
        result = 31 * result + subTotal.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ProdutoVendasEntity{" +
                "id=" + id +
                ", venda=" + venda +
                ", produto=" + produto +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", subTotal=" + subTotal +
                '}';
    }
}
