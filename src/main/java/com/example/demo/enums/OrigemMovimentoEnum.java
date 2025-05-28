package com.example.demo.enums;

public enum OrigemMovimentoEnum {
    VENDA("VENDA"),
    COMPRA("COMPRA"),
    AJUSTE("AJUSTE"),
    CADASTRO("CADASTRO");

    private String origemMovimentoEnum;


    OrigemMovimentoEnum(String origemMovimentoEnum) {
        this.origemMovimentoEnum = origemMovimentoEnum;
    }

    public String getOrigemMovimentoEnum(){
        return origemMovimentoEnum;
    }
}
