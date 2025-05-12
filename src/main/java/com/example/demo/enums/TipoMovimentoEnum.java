package com.example.demo.enums;

public enum TipoMovimentoEnum {
    ENTRADA("ENTRADA"),
    SAIDA("SAIDA"),
    AJUSTE("AJUSTE");

    private String tipoMovimentoEnum;

    TipoMovimentoEnum(String tipoMovimentoEnum) {
        this.tipoMovimentoEnum = tipoMovimentoEnum;
    }

    public String getTipoMovimentoEnum(){
        return tipoMovimentoEnum;
    }
}
