package com.invoice.service;

import com.invoice.enums.StatusVisita;
import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Visita;
import com.invoice.repository.ClienteRepository;
import com.invoice.repository.VisitaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitaService {

    private final VisitaRepository repository;

    private final ClienteService clienteService;

    private final EmpresaService empresaService;

    public Visita save(Visita visita){
        if(visita.getCliente().getId() == null){
            visita.setCliente(clienteService.save(visita.getCliente()));
        } else {
            visita.setCliente(clienteService.findById(visita.getCliente().getId()));
        }
        visita.setStatusVisita(StatusVisita.AGENDADO);
        return repository.save(visita);
    }

    public List<Visita> findAllByCliente(Long clienteId){
        Cliente cliente = clienteService.findById(clienteId);
        return repository.findAllByCliente(cliente);
    }

    public List<Visita> findAllByStatusAgendado(){
        return findAllByStatus(StatusVisita.AGENDADO);
    }

    public List<Visita> findAllByStatusVisitado(){
        return findAllByStatus(StatusVisita.VISITADO);
    }

    public List<Visita> findAllByStatusCancelado(){
        return findAllByStatus(StatusVisita.CANCELADO);
    }


    public List<Visita> findAll(){
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        return repository.findAllByCliente_Empresa_id(empresa.getId());
    }

    public Visita findById(Long idVisita) {
        return repository.findById(idVisita).get();
    }

    public Visita confirmarVisita(Long visitaId) {
        Visita visita = findById(visitaId);
        visita.setStatusVisita(StatusVisita.VISITADO);
        return repository.save(visita);
    }

    public Visita cancelarVisita(Long visitaId) {
        Visita visita = findById(visitaId);
        visita.setStatusVisita(StatusVisita.CANCELADO);
        return repository.save(visita);
    }

    private List<Visita> findAllByStatus(StatusVisita statusVisita){
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        return repository.findAllByStatusVisitaAndCliente_Empresa_id(statusVisita, empresa.getId());
    }
}
