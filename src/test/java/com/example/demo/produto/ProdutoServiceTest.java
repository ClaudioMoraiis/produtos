package com.example.demo.produto;

import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository fRepository;
    @Mock
    private ProdutoMovimentacaoRepository fProdutoMovimentacaoRepository;
    @Mock
    private ProdutoMapper fProdutoMapper;

    @InjectMocks
    private ProdutoService fService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Cadastro com sucesso")
    void cadastrar_sucesso() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Produto", BigDecimal.TEN, BigDecimal.TEN, 10f, "un", true);
        ProdutoEntity entity = new ProdutoEntity();
        entity.setEstoqueAtual(10f);

        when(fRepository.findFirstByNomeAndAtivoTrue(anyString())).thenReturn(null);
        when(fProdutoMapper.preencherProduto(dto)).thenReturn(entity);

        ResponseEntity<?> resp = fService.cadastrar(dto);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().toString().contains("Produto cadastrado com sucesso"));
        verify(fRepository).save(entity);
    }

    @Test
    @DisplayName("Cadastro com nome já existente")
    void cadastrar_nomeExistente() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Produto", BigDecimal.TEN, BigDecimal.TEN, 10f, "un", true);
        ProdutoEntity entity = new ProdutoEntity();
        entity.setAtivo(true);

        when(fRepository.findFirstByNomeAndAtivoTrue(anyString())).thenReturn(entity);

        ResponseEntity<?> resp = fService.cadastrar(dto);

        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertEquals("Já existe um produto com esse nome", resp.getBody());
    }

    @Test
    @DisplayName("Cadastro lança exceção")
    void cadastrar_excecao() {
        ProdutoCadastroDTO dto = new ProdutoCadastroDTO("Produto", BigDecimal.TEN, BigDecimal.TEN, 10f, "un", true);

        when(fRepository.findFirstByNomeAndAtivoTrue(anyString())).thenReturn(null);
        when(fProdutoMapper.preencherProduto(dto)).thenThrow(new RuntimeException("Erro"));

        ResponseEntity<?> resp = fService.cadastrar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().toString().contains("Erro ao cadastrar"));
    }

    @Test
    @DisplayName("Alterar produto com sucesso")
    void alterar_sucesso() {
        ProdutoAlterarDTO dto = new ProdutoAlterarDTO("NovoNome", "un", true);
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(1L);

        when(fRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(fRepository.save(any())).thenReturn(entity);

        ResponseEntity<?> resp = fService.alterar(dto, 1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().toString().contains("Produto alterado com sucesso"));
    }

    @Test
    @DisplayName("Alterar produto não encontrado")
    void alterar_naoEncontrado() {
        ProdutoAlterarDTO dto = new ProdutoAlterarDTO("NovoNome", "un", true);

        when(fRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = fService.alterar(dto, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(resp.getBody().toString().contains("Falha ao alterar"));
    }

    @Test
    @DisplayName("Buscar por ID - sucesso")
    void buscarPorId_sucesso() {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(1L);

        when(fRepository.findById(1L)).thenReturn(Optional.of(entity));

        ResponseEntity<?> resp = fService.buscarPorId(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertTrue(resp.getBody().toString().contains("Optional"));
    }

    @Test
    @DisplayName("Buscar por ID - não encontrado")
    void buscarPorId_naoEncontrado() {
        when(fRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = fService.buscarPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals("Nenhum produto localizado com esse id", resp.getBody());
    }

    @Test
    @DisplayName("Deletar produto com sucesso")
    void deletar_sucesso() {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setId(1L);
        entity.setAtivo(true);

        when(fRepository.findById(1L)).thenReturn(Optional.of(entity));

        ResponseEntity<?> resp = fService.deletar(1L);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Produto inativado com sucesso", resp.getBody());
        verify(fRepository).inativar(1L);
    }

    @Test
    @DisplayName("Deletar produto não encontrado ou inativo")
    void deletar_naoEncontradoOuInativo() {
        when(fRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = fService.deletar(1L);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertEquals("Nenhum produto localizado com esse id", resp.getBody());
    }

    @Test
    @DisplayName("Buscar por saldo - sem limite")
    void buscarPorSaldo_semLimite() {
        List<ProdutoEntity> lista = Arrays.asList(new ProdutoEntity());
        when(fRepository.findAll()).thenReturn(lista);

        ResponseEntity<?> resp = fService.buscarPorSaldo(null);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(lista, resp.getBody());
    }

    @Test
    @DisplayName("Buscar por saldo - com limite")
    void buscarPorSaldo_comLimite() {
        List<ProdutoEntity> lista = Arrays.asList(new ProdutoEntity());
        when(fRepository.buscarPorSaldo(5.0)).thenReturn(lista);

        ResponseEntity<?> resp = fService.buscarPorSaldo(5.0);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(lista, resp.getBody());
    }
}