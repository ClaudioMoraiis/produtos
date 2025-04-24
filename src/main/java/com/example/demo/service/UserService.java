package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository fRepository;

    @Autowired
    private PasswordEncoder fPasswordEncoder;

    public ResponseEntity<?> register(UserDTO mUserDTO){
        UserEntity mUserEntity = new UserEntity();

        String mEncryptedPassword = fPasswordEncoder.encode(mUserDTO.getSenha());
        mUserEntity.setSenha(mEncryptedPassword);
        mUserEntity.setEmail(mUserDTO.getEmail());
        fRepository.save(mUserEntity);
        return ResponseEntity.status(HttpStatus.OK).body("Usuário cadastro com sucesso");
    }
}
