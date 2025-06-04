package com.example.demo.enums;

public enum StatusCompraEnum {
    CONCLUIDA("CONCLUIDA"),
    CANCELADA("CANCELADA"),
    PENDENTE("PENDENTE");

    private String status;

    StatusCompraEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
