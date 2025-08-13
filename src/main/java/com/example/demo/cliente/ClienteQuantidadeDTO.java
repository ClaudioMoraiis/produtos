package com.example.demo.cliente;

public class ClienteQuantidadeDTO {
    private String nome;
    private Long quantidade;

    public ClienteQuantidadeDTO(String nome, Long quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }

    public ClienteQuantidadeDTO(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ClienteQuantidadeDTO{" +
                "nome='" + nome + '\'' +
                ", quantidade=" + quantidade +
                '}';
    }
}
