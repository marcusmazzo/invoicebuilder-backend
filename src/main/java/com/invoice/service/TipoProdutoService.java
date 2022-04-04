package com.invoice.service;

import com.invoice.model.Empresa;
import com.invoice.model.TipoProduto;
import com.invoice.repository.TipoProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoProdutoService {

    private final TipoProdutoRepository repository;

    private final EmpresaService empresaService;

    public TipoProduto save(TipoProduto tipoProduto){
        tipoProduto.setEmpresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))));
        return repository.save(tipoProduto);
    }

    public List<TipoProduto> findAll(){
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        return repository.findAllByEmpresa(empresa);
    }
}
