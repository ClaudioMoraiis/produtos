package com.example.demo.cliente;

import java.time.LocalDate;

public interface ClienteDados {
    String getNome();
    String getDocumento();
    LocalDate getDataNascimento();
    String getEmail();
    String getTelefone();
    String getEndereco();
    String getBairro();
    String getCidade();
    String getEstado();
    String getCep();
    Boolean getAtivo();
}
