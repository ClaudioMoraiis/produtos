package com.example.demo.cliente;

import com.example.demo.enums.TipoDocumentoEnum;
import com.example.demo.util.Util;

import java.time.LocalDate;

public class Cliente {
    public static ClienteEntity preencherVO(ClienteDados mCliente){
        ClienteEntity mClienteEntity = new ClienteEntity();
        mClienteEntity.setNome(mCliente.getNome());

        if (Util.validarTipoDocumento(mCliente.getDocumento()) == TipoDocumentoEnum.CNPJ)
            mClienteEntity.setTipoDocumento(TipoDocumentoEnum.CNPJ);
        else
            mClienteEntity.setTipoDocumento(TipoDocumentoEnum.CPF);

        mClienteEntity.setDocumento(Util.formatarDocumento(mCliente.getDocumento()));
        mClienteEntity.setDataNascimento(mCliente.getDataNascimento());
        mClienteEntity.setEmail(mCliente.getEmail());
        mClienteEntity.setTelefone(Util.formatarTelefone(mCliente.getTelefone()));
        mClienteEntity.setEndereco(mCliente.getEndereco());
        mClienteEntity.setBairro(mCliente.getBairro());
        mClienteEntity.setCidade(mCliente.getCidade());
        mClienteEntity.setEstado(mCliente.getEstado());
        mClienteEntity.setCep(mCliente.getCep());
        mClienteEntity.setAtivo(mCliente.getAtivo());
        mClienteEntity.setDataCadastro(LocalDate.now());
        mClienteEntity.setComplemento(mCliente.getComplemento());

        return mClienteEntity;
    }

    public static ClienteEntity alterar(ClienteEntity mClienteEntity, ClienteDados mCliente){
        mClienteEntity.setNome(mCliente.getNome());

        if (Util.validarTipoDocumento(mCliente.getDocumento()) == TipoDocumentoEnum.CNPJ)
            mClienteEntity.setTipoDocumento(TipoDocumentoEnum.CNPJ);
        else
            mClienteEntity.setTipoDocumento(TipoDocumentoEnum.CPF);

        mClienteEntity.setDocumento(Util.formatarDocumento(mCliente.getDocumento()));
        mClienteEntity.setDataNascimento(mCliente.getDataNascimento());
        mClienteEntity.setEmail(mCliente.getEmail());
        mClienteEntity.setTelefone(Util.formatarTelefone(mCliente.getTelefone()));
        mClienteEntity.setEndereco(mCliente.getEndereco());
        mClienteEntity.setBairro(mCliente.getBairro());
        mClienteEntity.setCidade(mCliente.getCidade());
        mClienteEntity.setEstado(mCliente.getEstado());
        mClienteEntity.setCep(mCliente.getCep());
        mClienteEntity.setAtivo(mCliente.getAtivo());
        mClienteEntity.setDataCadastro(LocalDate.now());
        mClienteEntity.setComplemento(mCliente.getComplemento());

        return mClienteEntity;
    }
}
