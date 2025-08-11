package com.example.demo.cliente;

import com.example.demo.produtoVendas.VendaRepository;
import com.example.demo.util.ResponseApiUtil;
import com.example.demo.venda.VendasEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private VendaRepository vendaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Cadastro com documento já existente")
    void testCadastrarDocumentoExistente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setDocumento("123.456.789-00");
        when(clienteRepository.findFirstByDocumentoOrderByIdAsc(anyString())).thenReturn(new ClienteEntity());

        ResponseEntity<?> response = clienteService.cadastrar(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Já existe cliente cadastrado com esse documento"));
    }

    @Test
    @DisplayName("Cadastro com e-mail já existente")
    void testCadastrarEmailExistente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setDocumento("123.456.789-00");
        dto.setEmail("test@email.com");
        when(clienteRepository.findFirstByDocumentoOrderByIdAsc(anyString())).thenReturn(null);
        when(clienteRepository.getEmail(dto.getEmail())).thenReturn(true);

        ResponseEntity<?> response = clienteService.cadastrar(dto);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Já existe cliente cadastrado com esse e-mail"));
    }

    @Test
    @DisplayName("Cadastro com documento inválido")
    void testCadastrarDocumentoInvalido() {
        ClienteDTO dto = new ClienteDTO();
        dto.setDocumento("123");
        when(clienteRepository.findFirstByDocumentoOrderByIdAsc(anyString())).thenReturn(null);
        when(clienteRepository.getEmail(anyString())).thenReturn(false);

        ResponseEntity<?> response = clienteService.cadastrar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Insira um documento válido"));
    }

    @Test
    @DisplayName("Cadastro com telefone inválido")
    void testCadastrarTelefoneInvalido() {
        ClienteDTO dto = new ClienteDTO();
        dto.setDocumento("12345678901");
        dto.setTelefone("123");
        when(clienteRepository.findFirstByDocumentoOrderByIdAsc(anyString())).thenReturn(null);
        when(clienteRepository.getEmail(anyString())).thenReturn(false);

        ResponseEntity<?> response = clienteService.cadastrar(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Preenche um numero de telefone válido"));
    }

    @Test
    @DisplayName("Cadastro com sucesso")
    void testCadastrarSucesso() {
        ClienteDTO dto = new ClienteDTO();
        dto.setDocumento("12345678901");
        dto.setTelefone("11999999999");
        dto.setEmail("test@email.com");
        dto.setNome("Cliente Teste");
        dto.setEndereco("Rua Teste, 123");
        dto.setBairro("Centro");
        dto.setCidade("São Paulo");
        dto.setEstado("SP");
        dto.setCep("01000-000");
        dto.setAtivo(true);
        dto.setComplemento("Apto 1");
        dto.setDataNascimento(LocalDate.of(2000, 1, 1));

        when(clienteRepository.findFirstByDocumentoOrderByIdAsc(anyString())).thenReturn(null);
        when(clienteRepository.getEmail(anyString())).thenReturn(false);

        ResponseEntity<?> response = clienteService.cadastrar(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Cliente cadastrado com sucesso"));
    }

    @Test
    @DisplayName("Listar clientes")
    void testListarClientes() {
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<?> response = clienteService.listarClientes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Listar vendas de cliente inexistente")
    void testListarVendasClienteInexistente() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> response = clienteService.listarVendas(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Nenhum cliente localizado"));
    }

    @Test
    @DisplayName("Buscar cliente por e-mail inexistente")
    void testBuscarPorEmailInexistente() {
        when(clienteRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ResponseEntity<?> response = clienteService.buscarPorEmail("naoexiste@email.com");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Nenhum cliente localizado com esse e-mail"));
    }

    @Test
    @DisplayName("Alterar cliente inexistente")
    void testAlterarClienteInexistente() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<?> response = clienteService.alterar(new ClienteDTO(), 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Nenhum cliente encontrado com esse id"));
    }

    @Test
    @DisplayName("Alterar cliente com documento inválido")
    void testAlterarClienteDocumentoInvalido() {
        ClienteEntity entity = new ClienteEntity();
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        ClienteDTO dto = new ClienteDTO();
        dto.setDocumento("123");

        ResponseEntity<?> response = clienteService.alterar(dto, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Insira um documento válido"));
    }

    @Test
    @DisplayName("Alterar cliente com telefone inválido")
    void testAlterarClienteTelefoneInvalido() {
        ClienteEntity entity = new ClienteEntity();
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(entity));
        ClienteDTO dto = new ClienteDTO();
        dto.setDocumento("12345678901");
        dto.setTelefone("123");

        ResponseEntity<?> response = clienteService.alterar(dto, 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Preencha um numero de telefone válido"));
    }
}