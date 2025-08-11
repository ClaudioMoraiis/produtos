 package com.example.demo.produtoCompras;
import com.example.demo.cliente.ClienteEntity;
import com.example.demo.compra.CompraEntity;
import com.example.demo.compra.CompraRepository;
import com.example.demo.compra.ListaCompraDTO;
import com.example.demo.enums.StatusCompraEnum;
import com.example.demo.produtoCompras.ProdutoCompraService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoCompraServiceTest {

    @InjectMocks
    private ProdutoCompraService produtoCompraService;

    @Mock
    private CompraRepository compraRepository;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Listar compras com sucesso")
    void testListarComprasComSucesso() {
        CompraEntity compra = new CompraEntity();
        compra.setId(1L);
        compra.setData(LocalDate.now());
        compra.setTotal(BigDecimal.TEN);
        compra.setStatus(StatusCompraEnum.PENDENTE);
        ClienteEntity cliente = new ClienteEntity();
        cliente.setId(1L);
        cliente.setNome("Cliente Teste");
        compra.setCliente(cliente);

        when(request.getParameterMap()).thenReturn(Collections.emptyMap());
        when(compraRepository.findAll()).thenReturn(List.of(compra));

        ResponseEntity<?> response = produtoCompraService.listar(null, null, null, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> lista = (List<?>) response.getBody();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
        Object compraRetornada = lista.get(0);
        assertEquals("Cliente Teste".toUpperCase(), ((ListaCompraDTO) compraRetornada).getCliente().getNome());
    }

    @Test
    @DisplayName("Listar compras com parâmetro inválido")
    void testListarComprasParametroInvalido() {
        Map<String, String[]> params = new HashMap<>();
        params.put("parametroInvalido", new String[]{"valor"});
        when(request.getParameterMap()).thenReturn(params);

        ResponseEntity<?> response = produtoCompraService.listar(null, null, null, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Utilize apenas os parâmetros"));
    }

    @Test
    @DisplayName("Listar compras com status inválido")
    void testListarComprasStatusInvalido() {
        when(request.getParameterMap()).thenReturn(Collections.emptyMap());

        ResponseEntity<?> response = produtoCompraService.listar(null, null, "INVALIDO", request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Parâmetro 'status' aceita apenas"));
    }

    @Test
    @DisplayName("Listar compras com data inicial sem data final")
    void testListarComprasDataInicialSemFinal() {
        when(request.getParameterMap()).thenReturn(Collections.emptyMap());

        ResponseEntity<?> response = produtoCompraService.listar("01/01/2024", null, null, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Informe uma data final"));
    }

    @Test
    @DisplayName("Listar compras com data final sem data inicial")
    void testListarComprasDataFinalSemInicial() {
        when(request.getParameterMap()).thenReturn(Collections.emptyMap());

        ResponseEntity<?> response = produtoCompraService.listar(null, "31/01/2024", null, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Informe uma data inicial"));
    }
}