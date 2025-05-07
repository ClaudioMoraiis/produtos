package com.example.demo.enums;

public enum SaleStatusEnum {
    CONCLUIDA(1),
    CANCELADA(2),
    PENDENTE(3);

    private Integer status;

    SaleStatusEnum(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
