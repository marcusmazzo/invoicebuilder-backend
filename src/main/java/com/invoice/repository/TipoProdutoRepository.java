package com.invoice.repository;

import com.invoice.model.Empresa;
import com.invoice.model.Produto;
import com.invoice.model.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoProdutoRepository extends JpaRepository<TipoProduto, Long> {

    List<TipoProduto> findAllByEmpresa(Empresa empresa);
}
