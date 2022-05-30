package com.invoice.controller;

import com.invoice.model.Documentos;
import com.invoice.service.DocumentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final DocumentoService documentoService;

    @PostMapping
    public List<Documentos> uploadFile(@RequestHeader("Authorization") String token,
                                       @RequestPart("pedido") String pedidoId,
                                       @RequestPart("files") List<MultipartFile> files){
        List<Documentos> list = new ArrayList<>();
        files.forEach(file -> {
            try {
                list.add(documentoService.salvarDocumentoOrcamento(Long.parseLong(pedidoId), file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return list;
    }

    @PostMapping("/imagem")
    public List<Documentos> uploadFileOrcamento(@RequestHeader("Authorization") String token,
                                       @RequestPart("pedido") String pedidoId,
                                       @RequestPart("files") List<MultipartFile> files){
        List<Documentos> list = new ArrayList<>();
        files.forEach(file -> {
            try {
                list.add(documentoService.salvarImagemOrcamento(Long.parseLong(pedidoId), file));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return list;
    }

    @GetMapping("/pedido/{id}")
    public ResponseEntity<List<Documentos>> findAll(@PathVariable("id") Long idPedido){
        return ResponseEntity.ok(documentoService.findAllByPedido(idPedido));
    }

    @GetMapping("/pedido/{id}/imagem")
    public ResponseEntity<List<Documentos>> findAllImages(@PathVariable("id") Long idPedido){
        return ResponseEntity.ok(documentoService.findAllImagesByPedido(idPedido));
    }
}
