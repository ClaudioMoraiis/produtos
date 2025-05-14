package com.example.demo.service;

import com.example.demo.dto.ProdutoCadastroDTO;
import com.example.demo.entity.ProdutoEntity;
import com.example.demo.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository fRepository;

    public ResponseEntity<?> cadastrar(ProdutoCadastroDTO mProdutoCadastroDTO){
        ProdutoEntity mProdutoEntity = new ProdutoEntity();

        mProdutoEntity.setNome(mProdutoCadastroDTO.getNome());
        mProdutoEntity.setAtivo(mProdutoCadastroDTO.getAtivo());
        mProdutoEntity.setUnidadeMedida(mProdutoCadastroDTO.getUnidadeMedida());
        mProdutoEntity.setDataCadastro(LocalDate.now());
        mProdutoEntity.setEstoqueAtual(mProdutoCadastroDTO.getEstoqueAtual());
        mProdutoEntity.setPrecoCusto(mProdutoCadastroDTO.getPrecoCusto());
        mProdutoEntity.setPrecoVenda(mProdutoCadastroDTO.getPrecoVenda());

        try {
            fRepository.save(mProdutoEntity);
            return ResponseEntity.status(HttpStatus.OK).body("Produto cadastrado com sucesso");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar\n" + e.getMessage());
        }
    }

}
