package testServices;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve cadastrar o usuário com sucesso")
    void testRegisterUserSuccess() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");
        userDTO.setSenha("testPassword");

        when(passwordEncoder.encode("testPassword")).thenReturn("encryptedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());

        ResponseEntity<?> response = userService.register(userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Usuário cadastrado com sucesso", response.getBody()); // Verifique se a string está correta

        verify(passwordEncoder).encode("testPassword");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve logar com sucesso")
    void testLoginSuccess() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");
        userDTO.setSenha("testPassword");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getSenha());

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authenticationToken);
        when(tokenService.generateToken(any())).thenReturn("fake-jwt-token");

        ResponseEntity<?> response = userService.login(userDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("fake-jwt-token", response.getBody());
    }

    @Test
    @DisplayName("Deve falhar ao logar com senha inválida")
    void testLoginInvalidPassword() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@gmail.com");
        userDTO.setSenha("wrongPassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@gmail.com");
        userEntity.setSenha("encryptedPassword");

        // Mock para retornar o usuário com a senha criptografada
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(userEntity);
        when(passwordEncoder.matches(userDTO.getSenha(), userEntity.getSenha())).thenReturn(false);

        // Espera que a exceção RuntimeException seja lançada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(userDTO);
        });

        assertEquals("Senha inválida", exception.getMessage());
    }
}
