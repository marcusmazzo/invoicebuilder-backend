package com.invoice.controller;

import com.invoice.model.Visita;
import com.invoice.service.VisitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/visita")
@RestController
public class VisitaController {

    private final VisitaService visitaService;

    @PostMapping("/save")
    public ResponseEntity<Visita> salvar(@RequestBody Visita visita){
        visita = visitaService.save(visita);
        return ResponseEntity.created(URI.create(visita.getId().toString())).build();
    }

    @GetMapping("/cliente/{id}")
    public ResponseEntity<List<Visita>> findAllbyCliente(@PathVariable("id") Long clienteId){
        return ResponseEntity.ok(visitaService.findAllByCliente(clienteId));
    }

    @GetMapping("/visitado")
    public ResponseEntity<List<Visita>> findAllbyClienteVisitado(){
        return ResponseEntity.ok(visitaService.findAllByStatusVisitado());
    }

    @GetMapping("/cancelado")
    public ResponseEntity<List<Visita>> findAllbyClienteCancelado(){
        return ResponseEntity.ok(visitaService.findAllByStatusCancelado());
    }

    @GetMapping("/agendado")
    public ResponseEntity<List<Visita>> findAllAgendado(){
        return ResponseEntity.ok(visitaService.findAllByStatusAgendado());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Visita> findById(@PathVariable("id") Long visitaId){
        return ResponseEntity.ok(visitaService.findById(visitaId));
    }

    @GetMapping("/cancelar/{id}")
    public ResponseEntity<Visita> cancelarVisita(@PathVariable("id") Long visitaId){
        return ResponseEntity.ok(visitaService.cancelarVisita(visitaId));
    }

    @GetMapping("/confirmar/{id}")
    public ResponseEntity<Visita> confirmarVisita(@PathVariable("id") Long visitaId){
        return ResponseEntity.ok(visitaService.confirmarVisita(visitaId));
    }

    @GetMapping
    public ResponseEntity<List<Visita>> findAll(){
        return ResponseEntity.ok(visitaService.findAll());
    }
}
