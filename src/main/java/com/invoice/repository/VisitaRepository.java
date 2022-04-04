package com.invoice.repository;

import com.invoice.enums.StatusVisita;
import com.invoice.model.Cliente;
import com.invoice.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, Long> {

    List<Visita> findAllByCliente(Cliente cliente);

    List<Visita> findAllByStatusVisitaAndCliente_Empresa_idOrderByDataVisita(StatusVisita statusVisita, Long empresaId);

    List<Visita> findAllByCliente_Empresa_idOrderByDataVisita(Long empresaId);
}
