package com.example.demo.cliente;

import com.example.demo.enums.TipoDocumentoEnum;
import com.example.demo.usuario.UsuarioEntity;
import com.example.demo.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository fRepository;

    public ResponseEntity<?> cadastrar(ClienteDTO mClienteDTO){
        try {
            ClienteEntity mUsuarioEntity = fRepository.findByDocumento(mClienteDTO.getDocumento());
            if (mUsuarioEntity != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe cliente cadastrado com esse documento");
            }

            fRepository.save(Cliente.preencherVO(mClienteDTO));
            return ResponseEntity.status(HttpStatus.OK).body("Cliente cadastrado com sucesso.");
        } catch (Exception e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar\n" + e.getMessage());
        }
    }
}
