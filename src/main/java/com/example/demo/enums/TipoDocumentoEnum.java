package com.example.demo.enums;

public enum TipoDocumentoEnum {
    CPF("CPF"),
    CNPJ("CNPJ");

    private String typeDocumentEnum;

    TipoDocumentoEnum(String typeDocumentEnum){
        this.typeDocumentEnum = typeDocumentEnum;
    }

    public String getTypeDocumentEnum(){
        return typeDocumentEnum;
    }

}
