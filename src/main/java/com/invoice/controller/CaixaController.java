package com.invoice.controller;

import com.invoice.model.Pagamento;
import com.invoice.service.CaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/caixa")
@RequiredArgsConstructor
public class CaixaController {

    private final CaixaService service;

    @PostMapping
    public ResponseEntity<Pagamento> save(@RequestBody Pagamento pagamento) {
        pagamento = service.salvar(pagamento);
        return ResponseEntity.created(URI.create(pagamento.getId().toString())).build();
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Pagamento>> findAllByClienteId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findAllByCliente(id));
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<List<Pagamento>> findAllByPedidoId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.findAllByPedido(id));
    }

}
