package com.invoice.repository;

import com.invoice.enums.TipoDocumento;
import com.invoice.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoRepository extends JpaRepository<Documentos, Long> {


    List<Documentos> findAllByPedidoAndTipoDocumento(Pedido pedido, TipoDocumento tipoDocumento);
}
