package com.invoice.service;

import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.repository.ClienteRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ClienteRepository repository;

    public Cliente save(Cliente cliente){
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        cliente.setEmpresa(empresa);
        return repository.save(cliente);
    }

    public Cliente findByTelemovel(String telemovel){
        return repository.findByContato(telemovel);
    }

    public Cliente findById(Long id) {
        Cliente cliente = repository.findById(id).get();
        return cliente;
    }

    public List<Cliente> findAll() {
        return repository.findAll();
    }

    public List<Cliente> findAllByEmpresa(Empresa empresa) {
        return repository.findAllByEmpresa(empresa);
    }
}
