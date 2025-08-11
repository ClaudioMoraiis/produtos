package com.example.demo.produto;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
    @Autowired
    private ProdutoService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ProdutoCadastroDTO mProdutoCadastroDTO){
        return fService.cadastrar(mProdutoCadastroDTO);
    }

    @PutMapping("/alterar/{id}")
    public ResponseEntity<?> alterar(@RequestBody ProdutoAlterarDTO mProdutoAlterarDTO, @PathVariable Long id){
        return fService.alterar(mProdutoAlterarDTO, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id){
        return fService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id){
        return fService.deletar(id);
    }

    @GetMapping("/por-saldo")
    public ResponseEntity buscarPorSaldo(@RequestParam(required = false) Double saldo){
        return fService.buscarPorSaldo(saldo);
    }
}
