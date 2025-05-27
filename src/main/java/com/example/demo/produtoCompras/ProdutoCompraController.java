package com.example.demo.produtoCompras;

import com.example.demo.compra.CompraRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compra")
public class ProdutoCompraController {
    @Autowired
    ProdutoCompraService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CompraRequestDTO mCompraRequestDTO){
        return fService.cadastrar(mCompraRequestDTO);
    }
}
