package com.invoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.service.ClienteService;
import com.invoice.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cliente")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    private final EmpresaService empresaService;

    @PostMapping("/salvar")
    public ResponseEntity<Cliente> save(@RequestBody Cliente cliente){
        cliente = service.save(cliente);
        return ResponseEntity.created(URI.create("http://localhost:8090/cliente/"+cliente.getId())).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getCliente(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @GetMapping("/contato/{contato}")
    public ResponseEntity<Cliente> getClienteByContato(@PathVariable("contato") String contato) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findByTelemovel(contato));
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() {
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByEmpresa(empresa));
    }

}
