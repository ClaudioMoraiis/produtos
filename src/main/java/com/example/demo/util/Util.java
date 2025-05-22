package com.example.demo.util;

import com.example.demo.enums.TipoDocumentoEnum;

public class Util {
    public static TipoDocumentoEnum validarTipoDocumento(String mDocumento){
       mDocumento = mDocumento.replaceAll("[./-]", "");

       return mDocumento.trim().length() == 14 ? TipoDocumentoEnum.CNPJ : TipoDocumentoEnum.CPF;
    }

    public static String formatarDocumento(String mDocumento){
        mDocumento = mDocumento.replaceAll("[./-]", "");
        if (mDocumento.trim().length() == 14)
            return  mDocumento.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        else
            return mDocumento.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
