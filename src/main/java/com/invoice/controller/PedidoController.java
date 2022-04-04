package com.invoice.controller;

import com.invoice.exception.PagamentoEmFaltaException;
import com.invoice.model.Cliente;
import com.invoice.model.Pedido;
import com.invoice.service.ClienteService;
import com.invoice.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping(value = "/salvar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pedido> save(@RequestBody Pedido pedido){
        pedido = service.save(pedido);
        return ResponseEntity.created(URI.create("http://localhost:8090/pedido/"+pedido.getId())).build();
    }

    @PostMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pedido> update(@RequestBody Pedido pedido){
        return ResponseEntity.ok(service.update(pedido));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> getPedido(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @GetMapping("/finalizar/{id}")
    public ResponseEntity<Pedido> finalizarPedido(@PathVariable("id") Long id) throws PagamentoEmFaltaException {
        return ResponseEntity.status(HttpStatus.OK).body(service.finalizarPedido(id));
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Pedido>> getPedidoByCliente(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByClienteId(id));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getPedidos(@PathParam("estado") Boolean estado) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAllByEstadoPedido(estado == null ? Boolean.FALSE: estado));
    }
}
