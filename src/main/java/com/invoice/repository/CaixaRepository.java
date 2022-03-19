package com.invoice.repository;

import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Pagamento;
import com.invoice.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CaixaRepository extends JpaRepository<Pagamento, Long> {


    List<Pagamento> findAllByEmpresa(Empresa empresa);

    List<Pagamento> findAllByCliente(Cliente cliente);

    List<Pagamento> findAllByPedido(Pedido byId);

    @Query(nativeQuery = true, value = "select max(numero) from pagamento where pedido_id = :pedido")
    Long maxNumero(@Param("pedido") Long id);

    @Query(nativeQuery = true, value = "select coalesce(null, sum(valor_pagamento), 0) from pagamento where pedido_id = :pedido")
    BigDecimal sumValorRecebido(@Param("pedido") Long id);
}
