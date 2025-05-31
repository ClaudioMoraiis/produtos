package com.example.demo.exceptions;

public class ProdutoNaoEncontradoException extends RuntimeException{
    public ProdutoNaoEncontradoException(Long mIdProduto) {
        super("Nenhum produto localizado com o ID: " + mIdProduto);
    }

}
