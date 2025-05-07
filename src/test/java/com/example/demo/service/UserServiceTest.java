package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.userRole.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository fRepository;

    @Mock
    private PasswordEncoder fPasswordEncoder;

    @Mock
    private AuthenticationManager fAuthenticationManager;

    @Mock
    private TokenService fTokenService;

    @InjectMocks
    private UserService fService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("User not found success")
    void testLoginUserNotFound(){
        UserDTO mUserDTO = new UserDTO("teste@gmail.com", "123");
        when(fRepository.findByEmail(mUserDTO.getEmail())).thenReturn(null);

        ResponseEntity<?> mResponse = fService.login(mUserDTO);

        assertEquals(HttpStatus.NOT_FOUND, mResponse.getStatusCode());
        assertEquals("Nenhum usuário encontrado", mResponse.getBody());
    }

    @Test
    @DisplayName("Login success")
    void testLoginSuccees(){
        UserDTO mUserDTO = new UserDTO("teste@gmail.com", "123");
        UserEntity mUserEntity = new UserEntity();
        mUserEntity.setEmail("TESTE@GMAIL.COM");
        mUserEntity.setSenha("senha123");
        // preenche a senha e cria o VO

        when(fRepository.findByEmail("teste@gmail.com")).thenReturn(mUserEntity);
        // Digamos que buscou pelo email e retorno o VO

        Authentication mAuthenticationMock = mock(Authentication.class);
        when(mAuthenticationMock.getPrincipal()).thenReturn(mUserEntity);
        when(fAuthenticationManager.authenticate(any())).thenReturn(mAuthenticationMock);
        //Fez as autenticações do login


        when(fTokenService.generateToken(mUserEntity)).thenReturn("mockedToken123");
        ResponseEntity<?> mResponse = fService.login(mUserDTO);
        // gerou um token ficticio e fez o login

        assertEquals(HttpStatus.OK, mResponse.getStatusCode()); //Comparou o status OK com o STATUS no Login no service
        assertTrue(mResponse.getBody().toString().contains("Login realizado com sucesso"));
        //Aqui comparou o que veio no body(o retorno) no service com a string passada, se igual o teste passa
        assertTrue(mResponse.getBody().toString().contains("Token :mockedToken123"));
        //Aqui verifica se no body veio o token ficticio
    }

    @Test
    @DisplayName("Sucesso ao criar usuário")
    void registerUserSuccess(){
        UserDTO mUserDTO = new UserDTO("test@gmail.com", "encryptedPassword");
        ResponseEntity<?> mResponse = fService.register(mUserDTO);

        assertEquals(HttpStatus.OK, mResponse.getStatusCode());
        assertTrue(mResponse.getBody().toString().contains("Usuário cadastrado com sucesso"));
    }

    @Test
    @DisplayName("Conflito de e-mail já cadastrado")
    void registerUserEmailAlreadyExists(){
        UserDTO mUserDTO = new UserDTO("test@gmail.com", "senha123");

        when(fRepository.existsByEmail("test@gmail.com")).thenReturn(true);
        ResponseEntity<?> mResponse = fService.register(mUserDTO);

        assertEquals(HttpStatus.CONFLICT, mResponse.getStatusCode());
        assertTrue(mResponse.getBody().toString().contains("E-mail já cadastrado, verifique!"));
    }
}