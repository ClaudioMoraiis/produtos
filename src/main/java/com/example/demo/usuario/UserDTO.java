package com.example.demo.usuario;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @JsonProperty("email")
    @NotNull(message = "Obrigatório preenchimento do campo email no body")
    private String email;

    @JsonProperty("senha")
    @NotNull(message = "Obrigatório preenchimento do campo senha no body")
    private String senha;

    public UserDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public UserDTO(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof UserDTO userDTO)) return false;

        return email.equals(userDTO.email) && senha.equals(userDTO.senha);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + senha.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }
}
