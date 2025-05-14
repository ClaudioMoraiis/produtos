package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "COMPRA")
public class CompraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "com_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "com_id_cliente", referencedColumnName = "cli_id", nullable = false)
    private ClienteEntity cliente;

    @Column(name = "com_data")
    private LocalDate data;

    @Column(name = "com_total")
    private BigDecimal total;

    public CompraEntity(Long id, ClienteEntity cliente, LocalDate data, BigDecimal total) {
        this.id = id;
        this.cliente = cliente;
        this.data = data;
        this.total = total;
    }

    public CompraEntity(){};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CompraEntity that)) return false;

        return cliente.equals(that.cliente) && data.equals(that.data) && total.equals(that.total);
    }

    @Override
    public int hashCode() {
        int result = cliente.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + total.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PurchaseEntity{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", data=" + data +
                ", total=" + total +
                '}';
    }


}
