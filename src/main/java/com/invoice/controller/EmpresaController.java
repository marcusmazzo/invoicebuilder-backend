package com.invoice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.invoice.jwt.JwtTokenUtil;
import com.invoice.model.Empresa;
import com.invoice.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/empresa")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    @PostMapping("/salvar/login")
    public ResponseEntity<Empresa> saveLogin(@RequestBody Empresa empresa){
        empresa = service.save(empresa);
        return ResponseEntity.created(URI.create("http://localhost:8090/empresa/"+empresa.getId())).build();
    }

    @PostMapping("/salvar")
    public ResponseEntity<Empresa> save(@RequestBody Empresa empresa){
        empresa = service.save(empresa);
        return ResponseEntity.created(URI.create("http://localhost:8090/empresa/"+empresa.getId())).build();
    }

    @PostMapping("/salvar-informacao")
    public ResponseEntity<Empresa> saveInformation(@RequestBody Empresa empresa){
        return ResponseEntity.ok().body(service.saveInformation(empresa.getInformacoes()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> getCliente(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<Empresa> getEmpresaByUser() throws JsonProcessingException, IllegalArgumentException {
        return ResponseEntity.status(HttpStatus.OK).body(service.findUserByToken());
    }

    @PostMapping("/logotipo")
    public ResponseEntity<String> uploadFile(@RequestParam("files") List<MultipartFile> files) throws IOException, ParseException {
        String base64 = service.saveLogotipo(files.get(0));
        return ResponseEntity.ok(base64);
    }

}
