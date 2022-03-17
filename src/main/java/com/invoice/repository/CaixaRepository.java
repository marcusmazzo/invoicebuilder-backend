package com.invoice.repository;

import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Pagamento;
import com.invoice.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaixaRepository extends JpaRepository<Pagamento, Long> {


    List<Pagamento> findAllByEmpresa(Empresa empresa);

    List<Pagamento> findAllByCliente(Cliente cliente);

    List<Pagamento> findAllByPedido(Pedido byId);
}
