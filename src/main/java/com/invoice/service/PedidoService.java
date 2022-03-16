package com.invoice.service;

import com.invoice.enums.EstadoPedido;
import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Pedido;
import com.invoice.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private EmpresaService empresaService;


    public Pedido save(Pedido pedido){
        if(pedido.getCliente().getId() == null){
            pedido.setCliente(clienteService.save(pedido.getCliente()));
        } else {
            pedido.setCliente(clienteService.findById(pedido.getCliente().getId()));
        }
        pedido.setEmpresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))));

        pedido.getItens().forEach(item -> {
            if(item.getValorVenda() == null) {
                BigDecimal percentual = item.getPedido().getEmpresa().getPercentualLucro().divide(new BigDecimal(100)).add(BigDecimal.ONE);
                item.setValorVenda(item.getCustoFabricacao().multiply(percentual));
                item.setTotalItem(item.getValorVenda().multiply(new BigDecimal(item.getQuantidade())));
            }
        });

        Calendar previsaoEntrega = Calendar.getInstance();
        previsaoEntrega.setTime(pedido.getDataPedido());
        previsaoEntrega.add(Calendar.DATE, pedido.getEmpresa().getPrazoMedioEntrega().intValue());
        pedido.setDataPrevisaoEntrega(previsaoEntrega.getTime());
        pedido.setEstadoPedido(EstadoPedido.PEDIDO_CRIADO);
        pedido.setConcluido(false);
        pedido.setTotalItensPedido(BigInteger.valueOf(pedido.getItens().size()));
        pedido.setCustoTotalPedido(pedido.getItens().stream().map(item -> item.getCustoFabricacao().multiply(new BigDecimal(item.getQuantidade()))).reduce((a, b) -> a.add(b)).get());
        pedido.setValorTotalPedido(pedido.getItens().stream().map(item -> item.getValorVenda().multiply(new BigDecimal(item.getQuantidade()))).reduce((a, b) -> a.add(b)).get());
        Long numero = repository.maxNumero(pedido.getEmpresa().getId());
        pedido.setNumero(numero == null ? 1L : numero+1L);
        pedido.setNumeroPedido(pedido.getNumero()+"/"+Calendar.getInstance().get(Calendar.YEAR));
        pedido = repository.save(pedido);

        return pedido;
    }

    public List<Pedido> findAllByClienteId(Long id){
        return repository.findAllByClienteIdOrderByIdDesc(id);
    }

    public Pedido findById(Long id){
        return repository.findById(id).get();
    }

    public List<Pedido> findAllByEstadoPedido(Boolean concluido){
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        return repository.findAllByEmpresaAndConcluido(empresa, concluido);
    }

}
