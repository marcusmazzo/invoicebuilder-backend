package com.invoice.controller;

import com.invoice.model.Produto;
import com.invoice.model.Visita;
import com.invoice.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/produto")
@RestController
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto){
        produto = produtoService.save(produto);
        return ResponseEntity.created(URI.create(produto.getId().toString())).build();
    }

    @GetMapping
    public ResponseEntity<List<Produto>> findAll(){
        return ResponseEntity.ok(produtoService.findAll());
    }
}
