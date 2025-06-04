package com.example.demo.compra;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.enums.StatusCompraEnum;
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

    @Column(name = "com_status")
    @Enumerated(EnumType.STRING)
    private StatusCompraEnum status = StatusCompraEnum.PENDENTE;

    public CompraEntity(Long id, ClienteEntity cliente, LocalDate data, BigDecimal total, StatusCompraEnum status) {
        this.id = id;
        this.cliente = cliente;
        this.data = data;
        this.total = total;
        this.status = status;
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

    public StatusCompraEnum getStatus() {
        return status;
    }

    public void setStatus(StatusCompraEnum status) {
        this.status = status;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof CompraEntity that)) return false;

        return cliente.equals(that.cliente) && data.equals(that.data) && total.equals(that.total) && status.equals(that.status);
    }

    @Override
    public int hashCode() {
        int result = cliente.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + total.hashCode();
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PurchaseEntity{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", data=" + data +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
