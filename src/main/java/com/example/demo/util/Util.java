package com.example.demo.util;

import com.example.demo.enums.TipoDocumentoEnum;

public class Util {
    public static TipoDocumentoEnum validarTipoDocumento(String mDocumento){
       mDocumento = mDocumento.replaceAll("[./-]", "");

       return mDocumento.trim().length() == 14 ? TipoDocumentoEnum.CNPJ : TipoDocumentoEnum.CNPJ;
    }
}
