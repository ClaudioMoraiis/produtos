package com.example.demo.venda;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.enums.StatusVendaEnum;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "VENDAS")
public class VendasEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ven_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ven_id_cliente", referencedColumnName = "cli_id", nullable = false)
    private ClienteEntity cliente;

    @Column(name = "ven_data_venda")
    private LocalDate dataVenda;

    @Column(name = "ven_total")
    private BigDecimal total;

    @Column(name = "ven_status")
    @Enumerated(EnumType.ORDINAL)
    private StatusVendaEnum status;

    public VendasEntity(Long id, ClienteEntity cliente, LocalDate dataVenda, BigDecimal total, StatusVendaEnum status) {
        this.id = id;
        this.cliente = cliente;
        this.dataVenda = dataVenda;
        this.total = total;
        this.status = status;
    }

    public VendasEntity(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteEntity getIdCliente() {
        return cliente;
    }

    public void setIdCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public StatusVendaEnum getStatus() {
        return status;
    }

    public void setStatus(StatusVendaEnum status) {
        this.status = status;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof VendasEntity that)) return false;

        return id.equals(that.id) && cliente.equals(that.cliente) && dataVenda.equals(that.dataVenda) && total.equals(that.total) && status == that.status;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + cliente.hashCode();
        result = 31 * result + dataVenda.hashCode();
        result = 31 * result + total.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SalesEntity{" +
                "id=" + id +
                ", idCliente=" + cliente +
                ", dataVenda=" + dataVenda +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
