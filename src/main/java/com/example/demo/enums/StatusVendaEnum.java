package com.example.demo.enums;

public enum StatusVendaEnum {
    CONCLUIDA(1),
    CANCELADA(2),
    PENDENTE(3);

    private Integer status;

    StatusVendaEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
