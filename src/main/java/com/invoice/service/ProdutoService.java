package com.invoice.service;

import com.invoice.enums.StatusVisita;
import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Produto;
import com.invoice.model.Visita;
import com.invoice.repository.ProdutoRepository;
import com.invoice.repository.VisitaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    private final EmpresaService empresaService;

    public Produto save(Produto produto){
        produto.setEmpresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))));
        return repository.save(produto);
    }

    public List<Produto> findAll(){
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        return repository.findAllByEmpresa(empresa);
    }
}
