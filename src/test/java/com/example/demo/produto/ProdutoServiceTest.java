package com.example.demo.produto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProdutoServiceTest {
    @Mock
    private ProdutoRepository fRepository;

    @InjectMocks
    private ProdutoService fService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Sucesso ao cadastrar")
    void successRegister(){
        ProdutoCadastroDTO mProdutoCadastroDTO = new ProdutoCadastroDTO(
                "produtoTeste", new BigDecimal(20), new BigDecimal(20), 20, "lata", true);
        ResponseEntity<?> mResponse = fService.cadastrar(mProdutoCadastroDTO);

        assertEquals(HttpStatus.OK, mResponse.getStatusCode());
        assertTrue(mResponse.getBody().toString().contains("Produto cadastrado com sucesso"));
    }

    @Test
    @DisplayName("Já existe produto com esse nome")
    void existsProductThisName(){
        ProdutoCadastroDTO mProdutoCadastroDTO = new ProdutoCadastroDTO(
                "produtoTeste", new BigDecimal(20), new BigDecimal(20), 20, "lata", true);

        when(fRepository.findByNome("produtoTeste")).thenReturn(new ProdutoEntity());
        ResponseEntity<?> mResponse = fService.cadastrar(mProdutoCadastroDTO);

        assertEquals(HttpStatus.CONFLICT, mResponse.getStatusCode());
        assertEquals("Já existe um produto com esse nome", mResponse.getBody());
    }
}