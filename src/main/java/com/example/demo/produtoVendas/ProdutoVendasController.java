package com.example.demo.produtoVendas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venda")
public class ProdutoVendasController {
    @Autowired
    private ProdutoVendasService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody VendaRequestDTO mDTO){
        return fService.cadastrar(mDTO);
    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id){
        return fService.cancelar(id);
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Long id){
        return fService.confirmar(id);
    }


}
