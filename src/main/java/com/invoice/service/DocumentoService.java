package com.invoice.service;

import com.invoice.enums.TipoDocumento;
import com.invoice.model.Documentos;
import com.invoice.model.Pedido;
import com.invoice.repository.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentoService {

    private final DocumentoRepository repository;

    private final EmpresaService empresaService;

    private final PedidoService pedidoService;

    public Documentos salvarDocumentoOrcamento(Long pedidoId, MultipartFile file) throws IOException {
        Pedido pedido = pedidoService.findById(pedidoId);
        Documentos documento = Documentos
                .builder()
                .pedido(pedido)
                .tipoDocumento(TipoDocumento.ORCAMENTO)
                .nomeDocumento(file.getOriginalFilename())
                .empresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))))
                .cliente(pedido.getCliente())
                .documento(file.getInputStream().readAllBytes())
                .build();

        return repository.save(documento);
    }

    public Documentos salvarImagemOrcamento(Long pedidoId, MultipartFile file) throws IOException {
        Pedido pedido = pedidoService.findById(pedidoId);
        Documentos documento = Documentos
                .builder()
                .pedido(pedido)
                .tipoDocumento(TipoDocumento.IMAGEM)
                .nomeDocumento(file.getOriginalFilename())
                .empresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))))
                .cliente(pedido.getCliente())
                .documento(file.getInputStream().readAllBytes())
                .build();

        return repository.save(documento);
    }

    public List<Documentos> findAllByPedido(Long pedidoId){
        List<Documentos> list = repository.findAllByPedidoAndTipoDocumento(pedidoService.findById(pedidoId), TipoDocumento.ORCAMENTO);
        if(list != null)
            list.forEach(documento -> documento.setContratoBase64(Base64.getEncoder().encodeToString(documento.getDocumento())));
        else
            list = new ArrayList<>();
        return list;
    }

    public List<Documentos> findAllImagesByPedido(Long pedidoId){
        List<Documentos> list = repository.findAllByPedidoAndTipoDocumento(pedidoService.findById(pedidoId), TipoDocumento.IMAGEM);
        if(list != null)
            list.forEach(documento -> documento.setContratoBase64(Base64.getEncoder().encodeToString(documento.getDocumento())));
        else
            list = new ArrayList<>();
        return list;
    }
}
