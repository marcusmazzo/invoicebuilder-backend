package com.invoice.service;

import com.invoice.model.Documentos;
import com.invoice.model.Pedido;
import com.invoice.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentoService {

    private final DocumentoRepository repository;

    private final EmpresaService empresaService;

    private final PedidoService pedidoService;

    public Documentos salvarDocumento(Long pedidoId, MultipartFile file) throws IOException {
        Pedido pedido = pedidoService.findById(pedidoId);
        Documentos documento = Documentos
                .builder()
                .pedido(pedido)
                .nomeDocumento("contrato.pdf")
                .empresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))))
                .cliente(pedido.getCliente())
                .documento(file.getInputStream().readAllBytes())
                .build();

        return repository.save(documento);
    }

    public List<Documentos> findAllByPedido(Long pedidoId){
        List<Documentos> list = repository.findAllByPedido(pedidoService.findById(pedidoId));
        list.forEach(documentos -> documentos.setContratoBase64(Base64.getEncoder().encodeToString(documentos.getDocumento())));
        return list;
    }
}
