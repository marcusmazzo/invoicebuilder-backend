package com.invoice.controller;

import com.invoice.model.TipoProduto;
import com.invoice.service.TipoProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/tipo-produto")
@RestController
public class TipoProdutoController {

    private final TipoProdutoService produtoService;

    @PostMapping
    public ResponseEntity<TipoProduto> salvar(@RequestBody TipoProduto tipoProduto){
        tipoProduto = produtoService.save(tipoProduto);
        return ResponseEntity.created(URI.create(tipoProduto.getId().toString())).build();
    }

    @GetMapping
    public ResponseEntity<List<TipoProduto>> findAll(){
        return ResponseEntity.ok(produtoService.findAll());
    }
}
