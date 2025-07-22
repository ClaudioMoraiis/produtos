package com.example.demo.util;

import com.example.demo.enums.TipoDocumentoEnum;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    public static String formatarTelefone(String mNumero){
        try {
            mNumero = mNumero.trim().replaceAll("[-/.()]", "");
            MaskFormatter maskFormatter = new MaskFormatter("(##)#####-####");
            maskFormatter.setValueContainsLiteralCharacters(false);

            return maskFormatter.valueToString(mNumero);
        } catch (ParseException e){
            e.printStackTrace();
            return mNumero;
        }
    };

    public static LocalDate formatarData(String mDataEntrada){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(mDataEntrada, formatter);
        } catch (DateTimeParseException e) {
            System.err.println("Data inválida: " + mDataEntrada);
            return null;
        }
    }
}
