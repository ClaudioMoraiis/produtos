package com.example.demo.cliente;

import com.example.demo.enums.StatusVendaEnum;
import com.example.demo.produtoVendas.VendaRepository;
import com.example.demo.produtoVendas.VendasDTO;
import com.example.demo.util.ResponseApiUtil;
import com.example.demo.util.Util;
import com.example.demo.venda.VendasEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository fRepository;

    @Autowired
    private VendaRepository fVendaRepository;

    public ResponseEntity<?> cadastrar(ClienteDTO mClienteDTO){
        try {
            ClienteEntity mUsuarioEntity = fRepository.findFirstByDocumentoOrderByIdAsc(Util.formatarDocumento(mClienteDTO.getDocumento()));
            if (mUsuarioEntity != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe cliente cadastrado com esse documento");
            }

            if (fRepository.getEmail(mClienteDTO.getEmail())){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe cliente cadastrado com esse e-mail");
            }

            String mDocumentoFormatado = mClienteDTO.getDocumento().replaceAll("[./-]", "");
            if ((mDocumentoFormatado.length() != 11) && (mDocumentoFormatado.length() != 14))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insira um documento válido");

            if (mClienteDTO.getTelefone().trim().replaceAll("[-/()]", "").length() != 11){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preenche um numero de telefone válido");
            }

            fRepository.save(Cliente.preencherVO(mClienteDTO));
            return ResponseEntity.status(HttpStatus.OK).body("Cliente cadastrado com sucesso.");
        } catch (Exception e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar\n" + e.getMessage());
        }
    }

    public ResponseEntity<?> listarClientes(){
        return ResponseEntity.status(HttpStatus.OK).body(fRepository.findAll());
    }

    public ResponseEntity<?> listarVendas(Long mId){
        Optional<ClienteEntity> mClienteEntity = fRepository.findById(mId);
        if (mClienteEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum cliente localizado");
        }

        List<VendasEntity> mListaVendas = fVendaRepository.listarPorCliente(mId);
        List<VendasDTO> mListaVendasDTO = mListaVendas.stream().
                map(v -> new VendasDTO(v.getId(), v.getTotal(), v.getDataVenda(), String.valueOf(v.getStatus()), null))
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(mListaVendasDTO);
    }

    public ResponseEntity<?> buscarPorEmail(String mEmail){
        Optional<ClienteEntity> mClienteEntity = fRepository.findByEmail(mEmail);
        if (mClienteEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum cliente localizado com esse e-mail");
        }

        return ResponseEntity.status(HttpStatus.OK).body(mClienteEntity);
    }

    public ResponseEntity<?> alterar(ClienteDTO mDto, Long mId){
        Optional<ClienteEntity> mClienteEntity = fRepository.findById(mId);
        if (mClienteEntity.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    ResponseApiUtil.response("Erro", "Nenhum cliente encontrado com esse id")
            );
        }

        String mDocumentoFormatado = mDto.getDocumento().replaceAll("[./-]", "");
        if ((mDocumentoFormatado.length() != 11) && (mDocumentoFormatado.length() != 14))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseApiUtil.response("Erro", "Insira um documento válido")
            );

        if (mDto.getTelefone().trim().replaceAll("[-/()]", "").length() != 11){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ResponseApiUtil.response("Erro", "\"Preencha um numero de telefone válido\"")
            );
        }

        fRepository.save(Cliente.alterar(mClienteEntity.get(), mDto));
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseApiUtil.response("Sucesso", "Cliente alterado.")
        );
    }

    public ResponseEntity<?> buscarTop5() {
        List<ClienteQuantidadeDTO> mResultados = fRepository.buscarTop5Clientes();

        List<ClienteQuantidadeDTO> novaLista = mResultados.stream()
                .map(mI -> {
                    ClienteQuantidadeDTO mDto = new ClienteQuantidadeDTO();
                    mDto.setQuantidade(mI.getQuantidade());
                    mDto.setNome(mI.getNome());
                    return mDto;
                }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(novaLista);
       }
    }
