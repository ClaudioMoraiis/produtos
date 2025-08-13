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

    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email){
        return fService.buscarPorEmail(email);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@RequestBody ClienteDTO mDto, @PathVariable Long id){
        return fService.alterar(mDto, id);
    }

    @GetMapping("/mais-ativos")
    public ResponseEntity<?> buscarTop5(){
        return fService.buscarTop5();
    }

}
