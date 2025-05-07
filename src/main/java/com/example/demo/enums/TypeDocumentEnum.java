package com.example.demo.enums;

public enum TypeDocumentEnum {
    CPF("CPF"),
    CNPJ("CNPJ");

    private String typeDocumentEnum;

    TypeDocumentEnum(String typeDocumentEnum){
        this.typeDocumentEnum = typeDocumentEnum;
    }

    public String getTypeDocumentEnum(){
        return typeDocumentEnum;
    }

}
