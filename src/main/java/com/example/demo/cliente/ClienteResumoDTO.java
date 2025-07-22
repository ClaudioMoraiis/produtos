package com.example.demo.cliente;

public class ClienteResumoDTO {
    private long id;
    private String nome;

    public ClienteResumoDTO(long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public ClienteResumoDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
