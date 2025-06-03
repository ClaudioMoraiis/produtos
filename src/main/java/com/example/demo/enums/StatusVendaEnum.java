package com.example.demo.enums;

public enum StatusVendaEnum {
    CONCLUIDA("CONCLUIDA"),
    CANCELADA("CANCELADA"),
    PENDENTE("PENDENTE");

    private String status;

    StatusVendaEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
