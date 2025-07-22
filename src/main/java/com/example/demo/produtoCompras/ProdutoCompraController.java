package com.example.demo.produtoCompras;

import com.example.demo.compra.CompraRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/compra")
public class ProdutoCompraController {
    @Autowired
    ProdutoCompraService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CompraRequestDTO mCompraRequestDTO){
        return fService.cadastrar(mCompraRequestDTO);
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dataInicial,
            @RequestParam(required = false) String dataFinal,
            HttpServletRequest request
    ){
        return fService.listar(dataInicial, dataFinal, status, request);
    }
}
