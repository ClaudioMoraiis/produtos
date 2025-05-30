package com.example.demo.usuario;

import com.example.demo.enums.UserRoleEnum;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USUARIO")
public class UsuarioEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usu_name")
    private Long id;

    @Column(name = "usu_email")
    private String email;

    @Column(name = "usu_senha")
    private String senha;

    @Column(name = "usu_role")
    private UserRoleEnum role;

    public UsuarioEntity(){}

    public UsuarioEntity(Long id, String email, String senha, UserRoleEnum userRole) {
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.role = userRole;
    }

    public UsuarioEntity(UserDTO mUserDTO){
        this.email = mUserDTO.getEmail();
        this.senha = mUserDTO.getSenha();
    }

    @PrePersist
    @PreUpdate
    private void upperCase(){
        if (email != null)
            email = email.toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        if (!(o instanceof UsuarioEntity usuario)) return false;

        return id.equals(usuario.id) && email.equals(usuario.email) && senha.equals(usuario.senha);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + senha.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRoleEnum.ADMIN) return List.of(
                                                           new SimpleGrantedAuthority("ROLE_ADMIN"),
                                                           new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
