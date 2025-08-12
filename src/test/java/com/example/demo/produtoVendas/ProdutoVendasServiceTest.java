// src/test/java/com/example/demo/produtoVendas/ProdutoVendasServiceTest.java
package com.example.demo.produtoVendas;

import com.example.demo.cliente.ClienteEntity;
import com.example.demo.cliente.ClienteRepository;
import com.example.demo.enums.OrigemMovimentoEnum;
import com.example.demo.enums.StatusVendaEnum;
import com.example.demo.enums.TipoMovimentoEnum;
import com.example.demo.produto.ProdutoEntity;
import com.example.demo.produto.ProdutoMapper;
import com.example.demo.produto.ProdutoRepository;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoEntity;
import com.example.demo.produtoMovimentacao.ProdutoMovimentacaoRepository;
import com.example.demo.util.ResponseApiUtil;
import com.example.demo.venda.VendasEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoVendasServiceTest {

    @InjectMocks
    private ProdutoVendasService service;

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ProdutoVendaRepository produtoVendaRepository;
    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private ProdutoMapper produtoMapper;
    @Mock
    private ProdutoMovimentacaoRepository produtoMovimentacaoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ProdutoVendasDTO buildProdutoVendasDTO(Long idProduto, float quantidade, BigDecimal preco) {
        ProdutoVendasDTO dto = new ProdutoVendasDTO();
        dto.setId_produto(idProduto);
        dto.setQuantidade(quantidade);
        dto.setPreco_unitario(preco);
        return dto;
    }

    @Test
    void cadastrar_DeveCadastrarVendaComSucesso() {
        Long clienteId = 1L, produtoId = 2L;
        ClienteEntity cliente = new ClienteEntity(); cliente.setId(clienteId);
        ProdutoEntity produto = new ProdutoEntity(); produto.setId(produtoId); produto.setNome("P"); produto.setEstoqueAtual(10f);
        ProdutoVendasDTO dto = buildProdutoVendasDTO(produtoId, 2f, BigDecimal.TEN);
        VendaRequestDTO req = new VendaRequestDTO(); req.setId_cliente(clienteId); req.setStatus("PENDENTE"); req.setLista_produto(List.of(dto));
        VendasEntity venda = new VendasEntity(); venda.setId(3L); venda.setStatus(StatusVendaEnum.PENDENTE);

        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoMapper.preencherProdutoMovEntity(any(), any(), any(), any(), any())).thenReturn(new ProdutoMovimentacaoEntity());
        when(produtoVendaRepository.saveAll(any())).thenReturn(List.of());
        when(produtoMovimentacaoRepository.saveAll(any())).thenReturn(List.of());
        doAnswer(inv -> { ((VendasEntity)inv.getArgument(0)).setId(3L); return null; }).when(vendaRepository).save(any());

        ResponseEntity<?> resp = service.cadastrar(req);

        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().toString().contains("sucesso"));
    }

    @Test
    void cadastrar_DeveRetornarErroClienteNaoEncontrado() {
        VendaRequestDTO req = new VendaRequestDTO(); req.setId_cliente(1L);
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = service.cadastrar(req);
        assertEquals(400, resp.getStatusCodeValue());
    }

    @Test
    void cancelar_DeveCancelarVendaConcluida() {
        Long vendaId = 1L, produtoId = 2L;
        VendasEntity venda = new VendasEntity(); venda.setId(vendaId); venda.setStatus(StatusVendaEnum.CONCLUIDA);
        ProdutoEntity produto = new ProdutoEntity(); produto.setId(produtoId); produto.setNome("P");
        ProdutoVendasEntity prodVenda = new ProdutoVendasEntity(); prodVenda.setProduto(produto); prodVenda.setQuantidade(2f);
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(venda));
        when(produtoVendaRepository.getVendas(vendaId)).thenReturn(List.of(prodVenda));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoMapper.preencherProdutoMovEntity(any(), any(), any(), any(), any())).thenReturn(new ProdutoMovimentacaoEntity());
        when(produtoMovimentacaoRepository.saveAll(any())).thenReturn(List.of());
        when(vendaRepository.save(any())).thenReturn(venda);

        ResponseEntity<?> resp = service.cancelar(vendaId);

        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().toString().contains("Sucesso"));
    }

    @Test
    void cancelar_DeveRetornarErroVendaNaoEncontrada() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = service.cancelar(1L);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void confirmar_DeveConfirmarVendaComSucesso() {
        Long vendaId = 1L, produtoId = 2L;
        VendasEntity venda = new VendasEntity(); venda.setId(vendaId); venda.setStatus(StatusVendaEnum.PENDENTE);
        ProdutoEntity produto = new ProdutoEntity(); produto.setId(produtoId); produto.setNome("P"); produto.setEstoqueAtual(10f);
        ProdutoVendasEntity prodVenda = new ProdutoVendasEntity(); prodVenda.setProduto(produto); prodVenda.setQuantidade(2f);
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(venda));
        when(produtoVendaRepository.getVendas(vendaId)).thenReturn(List.of(prodVenda));
        when(produtoRepository.findAllById(any())).thenReturn(List.of(produto));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoMapper.preencherProdutoMovEntity(any(), any(), any(), any(), any())).thenReturn(new ProdutoMovimentacaoEntity());
        when(produtoMovimentacaoRepository.saveAll(any())).thenReturn(List.of());
        when(vendaRepository.save(any())).thenReturn(venda);

        ResponseEntity<?> resp = service.confirmar(vendaId);

        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().toString().contains("Venda confirmada"));
    }

    @Test
    void confirmar_DeveRetornarErroVendaNaoEncontrada() {
        when(vendaRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = service.confirmar(1L);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void alterar_DeveAlterarVendaComSucesso() {
        Long vendaId = 1L, clienteId = 2L, produtoId = 3L;
        ProdutoVendasDTO dto = buildProdutoVendasDTO(produtoId, 2f, BigDecimal.TEN);
        VendaRequestDTO req = new VendaRequestDTO(); req.setId_cliente(clienteId); req.setStatus("CONCLUIDA"); req.setLista_produto(List.of(dto));
        VendasEntity venda = new VendasEntity(); venda.setId(vendaId); venda.setStatus(StatusVendaEnum.PENDENTE);
        ClienteEntity cliente = new ClienteEntity(); cliente.setId(clienteId);
        ProdutoEntity produto = new ProdutoEntity(); produto.setId(produtoId); produto.setNome("P"); produto.setEstoqueAtual(10f);

        when(vendaRepository.getStatus(vendaId)).thenReturn(StatusVendaEnum.PENDENTE);
        when(vendaRepository.findById(vendaId)).thenReturn(Optional.of(venda));
        when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));
        when(produtoRepository.findAllById(any())).thenReturn(List.of(produto));
        when(produtoMapper.preencherProdutoMovEntity(any(), any(), any(), any(), any())).thenReturn(new ProdutoMovimentacaoEntity());
        when(produtoVendaRepository.saveAll(any())).thenReturn(List.of());
        when(produtoMovimentacaoRepository.saveAll(any())).thenReturn(List.of());

        ResponseEntity<?> resp = service.alterar(req, vendaId);

        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().toString().contains("alterada com sucesso"));
    }

    @Test
    void alterar_DeveRetornarErroVendaNaoEncontrada() {
        VendaRequestDTO req = new VendaRequestDTO(); req.setId_cliente(1L); req.setLista_produto(List.of());
        when(vendaRepository.findById(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> resp = service.alterar(req, 1L);
        assertEquals(404, resp.getStatusCodeValue());
    }
}