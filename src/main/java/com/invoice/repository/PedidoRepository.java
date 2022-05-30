package com.invoice.repository;

import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByClienteIdOrderByIdDesc(Long id);

    List<Pedido> findAllByEmpresaAndConcluido(Empresa empresa, Boolean concluido);

    @Query(nativeQuery = true, value = "select coalesce(null, max(numero), 1) from pedido where empresa_id = :empresa")
    Long maxNumero(@Param("empresa") Long id);
}
