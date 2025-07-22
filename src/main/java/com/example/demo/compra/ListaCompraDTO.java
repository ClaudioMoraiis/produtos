package com.example.demo.compra;

import com.example.demo.cliente.ClienteResumoDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@JsonPropertyOrder({"id", "data", "total", "status", "cliente"})
public class ListaCompraDTO {
    @JsonProperty("data")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull(message = "Campo 'data' não preechido no body, verifique")
    private LocalDate data;

    @JsonProperty("total")
    private BigDecimal total;

    private ClienteResumoDTO cliente;

    private String status;

    private Long id;

    public ListaCompraDTO(){};

    public ListaCompraDTO(LocalDate data, BigDecimal total, String status, Long id) {
        this.data = data;
        this.total = total;
        this.status = status;
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public ClienteResumoDTO getCliente() {
        return cliente;
    }

    public void setcliente(ClienteResumoDTO cliente) {
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ListaCompraDTO compraDTO)) return false;

        return data.equals(compraDTO.data) && total.equals(compraDTO.total);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(data);
        result = 31 * result + Objects.hashCode(total);
        result = 31 * result + Objects.hashCode(cliente);
        return result;
    }

    @Override
    public String toString() {
        return "ListaCompraDTO{" +
                "data=" + data +
                ", total=" + total +
                ", cliente=" + cliente +
                '}';
    }
}
