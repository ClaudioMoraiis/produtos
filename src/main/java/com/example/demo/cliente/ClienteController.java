package com.example.demo.cliente;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    @Autowired
    private ClienteService fService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody @Valid ClienteDTO mClienteDTO){
        return fService.cadastrar(mClienteDTO);
    }

    @GetMapping("/clientes")
    public ResponseEntity<?> listarClientes(){
        return fService.listarClientes();
    }

    @GetMapping("/{id}/vendas")
    public ResponseEntity<?> listarVendas(@PathVariable Long id){
        return  fService.listarVendas(id);
    }
}
