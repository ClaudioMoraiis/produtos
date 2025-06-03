package com.example.demo.produtoVendas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/venda")
public class ProdutoVendasController {
    @Autowired
    private ProdutoVendasService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody VendaRequestDTO mDTO){
        return fService.cadastrar(mDTO);
    }


}
