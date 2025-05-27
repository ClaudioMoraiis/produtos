package com.example.demo.compra;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CompraDTO {
    @JsonProperty("id_cliente")
    @NotNull(message = "Campo 'id_cliente' não preenchido no body, verifique.")
    private Long id_cliente;

    @JsonProperty("data_compra")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull(message = "Campo 'data_compra' não preechido no body, verifique")
    private LocalDate data;

    @JsonProperty("total")
    private BigDecimal total;

    public CompraDTO(){};

    public CompraDTO(Long id_cliente, LocalDate data, BigDecimal total) {
        this.id_cliente = id_cliente;
        this.data = data;
        this.total = total;
    }

    public Long getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(Long id_cliente) {
        this.id_cliente = id_cliente;
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
        if (!(o instanceof CompraDTO compraDTO)) return false;

        return id_cliente.equals(compraDTO.id_cliente) && data.equals(compraDTO.data) && total.equals(compraDTO.total);
    }

    @Override
    public int hashCode() {
        int result = id_cliente.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + total.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CompraDTO{" +
                "id_cliente=" + id_cliente +
                ", data=" + data +
                ", total=" + total +
                '}';
    }
}
