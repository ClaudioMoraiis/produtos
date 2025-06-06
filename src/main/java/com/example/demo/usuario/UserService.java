package com.example.demo.usuario;

import com.example.demo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository fRepository;

    @Autowired
    private PasswordEncoder fPasswordEncoder;

    @Autowired
    private AuthenticationManager fAuthenticationManager;

    @Autowired
    private TokenService fTokenService;

    public ResponseEntity<?> register(UserDTO mUserDTO){
        UsuarioEntity mUserEntity = new UsuarioEntity();

        if (fRepository.existsByEmail(mUserDTO.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("E-mail já cadastrado, verifique!");

        String mEncryptedPassword = fPasswordEncoder.encode(mUserDTO.getSenha());
        mUserEntity.setSenha(mEncryptedPassword);
        mUserEntity.setEmail(mUserDTO.getEmail());
        fRepository.save(mUserEntity);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário cadastrado com sucesso");
    }

    public ResponseEntity<?> login(UserDTO mUserDTO){
        UsuarioEntity mUserEntity = fRepository.findByEmail(mUserDTO.getEmail());
        try{
            if (mUserEntity == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário encontrado");
            }

            var mAuthToken = new UsernamePasswordAuthenticationToken(mUserDTO.getEmail().toUpperCase(), mUserDTO.getSenha());
            var mAuth = fAuthenticationManager.authenticate(mAuthToken);
            var mToken = fTokenService.generateToken((UsuarioEntity) mAuth.getPrincipal());


            return ResponseEntity.status(HttpStatus.OK).body("Login realizado com sucesso\n" + "Token :" + mToken);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erro " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
}
