package com.invoice.service;

import com.invoice.enums.EstadoPedido;
import com.invoice.exception.PagamentoEmFaltaException;
import com.invoice.model.Empresa;
import com.invoice.model.Pagamento;
import com.invoice.model.Pedido;
import com.invoice.repository.CaixaRepository;
import com.invoice.repository.PedidoRepository;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoRepository repository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private CaixaRepository caixaService;

    public Pedido update(Pedido pedido){
        return repository.save(pedido);
    }


    public Pedido save(Pedido pedido){
        if(pedido.getCliente().getId() == null){
            pedido.setCliente(clienteService.save(pedido.getCliente()));
        } else {
            pedido.setCliente(clienteService.findById(pedido.getCliente().getId()));
        }
        pedido.setEmpresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))));
        Long numero = repository.maxNumero(pedido.getEmpresa().getId());

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
        pedido.setValorTotalPedidoIva(pedido.getValorTotalPedido().add(pedido.getValorTotalPedido().multiply(pedido.getEmpresa().getPercentualIva()).divide(BigDecimal.valueOf(100), RoundingMode.FLOOR)));
        pedido.setNumero(numero == null ? 1L : numero+1L);
        pedido.setNumeroPedido(pedido.getNumero()+"/"+Calendar.getInstance().get(Calendar.YEAR));
        pedido.setPagamentoComIva(Boolean.FALSE);
        return repository.save(pedido);
    }

    public List<Pedido> findAllByClienteId(Long id){

        List<Pedido> pedidos = repository.findAllByClienteIdOrderByIdDesc(id);
        pedidos.stream().forEach(pedido -> pedido.setStatusPedido(pedido.getEstadoPedido().getDescricao()));
        return pedidos;
    }

    public Pedido findById(Long id){
        Pedido pedido = repository.findById(id).get();
        pedido.setStatusPedido(pedido.getEstadoPedido().getDescricao());
        return pedido;
    }

    public List<Pedido> findAllByEstadoPedido(Boolean concluido){
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        List<Pedido> pedidos = repository.findAllByEmpresaAndConcluido(empresa, concluido);
        pedidos.stream().forEach(pedido -> {
            pedido.setStatusPedido(pedido.getEstadoPedido().getDescricao());
            BigDecimal valorTotalPago = BigDecimal.ZERO;
            for (Pagamento pagamento : pedido.getPagamentos()) {
                valorTotalPago = valorTotalPago.add(pagamento.getValorRecebido());
            }
            pedido.setValorTotalPago(valorTotalPago);
        });
        return pedidos;
    }

    public Pedido finalizarPedido(Long id) throws PagamentoEmFaltaException {
        Pedido pedido = findById(id);
        BigDecimal totalRecebido = caixaService.sumValorRecebido(pedido.getId());
        if(totalRecebido.compareTo(pedido.getPagamentoComIva() ? pedido.getValorTotalPedidoIva() : pedido.getValorTotalPedido()) < 0){
            throw new PagamentoEmFaltaException("O pedido ainda não foi completamente pago");
        }
        pedido.setEstadoPedido(EstadoPedido.CONCLUIDO);
        pedido.setDataEntrega(new Date());
        return repository.save(pedido);
    }

}
