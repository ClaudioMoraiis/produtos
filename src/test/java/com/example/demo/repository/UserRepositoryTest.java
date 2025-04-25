package com.example.demo.repository;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository fUsuarioRepository;

    @Autowired
    EntityManager fEntityManager;

    @Test
    @DisplayName("Exist the user in DataBase")
    void findByEmailSucecess() {
      UserDTO mUserDTO = new UserDTO("teste@gmai.com", "123");
      this.createUser(mUserDTO);

      Optional<UserEntity> mResult = Optional.ofNullable(this.fUsuarioRepository.findByEmail(mUserDTO.getEmail()));

      assertThat(mResult.isPresent()).isTrue();
    }

    private UserEntity createUser(UserDTO mUserDTO){
      UserEntity mUser = new UserEntity(mUserDTO);
      this.fEntityManager.persist(mUser);
      return mUser;
    }
}