package com.example.demo.cliente;

import com.example.demo.enums.TipoDocumentoEnum;
import com.example.demo.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClienteServiceTest {
    @Mock
    private ClienteRepository fRepository;

    @InjectMocks
    private ClienteService fService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Já existe documento cadastrado")
    void shouldFailWhenDocumentAlreadyExists() {
        ClienteDTO mClienteDTO = new ClienteDTO(
                "Teste", "11092895952", TipoDocumentoEnum.CNPJ, LocalDate.now(),
                "teste@gmail.com", "48999999999", "Endereço teste", "teste", "teste",
                "braco do norte", "SC", "88750000", true
        );

        when(fRepository.findFirstByDocumentoOrderByIdAsc(Util.formatarDocumento(mClienteDTO.getDocumento())))
                .thenReturn(new ClienteEntity());

        ResponseEntity<?> response = fService.cadastrar(mClienteDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Já existe cliente cadastrado com esse documento", response.getBody());
    }

}