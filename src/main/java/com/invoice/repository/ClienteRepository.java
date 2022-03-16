package com.invoice.repository;

import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findAllByEmpresa(Empresa empresa);

    Cliente findByContato(String telemovel);
}
