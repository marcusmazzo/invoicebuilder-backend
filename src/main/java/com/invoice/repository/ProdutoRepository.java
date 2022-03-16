package com.invoice.repository;

import com.invoice.enums.StatusVisita;
import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Produto;
import com.invoice.model.Visita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findAllByEmpresa(Empresa empresa);
}
