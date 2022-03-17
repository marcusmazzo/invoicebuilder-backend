package com.invoice.service;

import com.invoice.enums.EstadoPedido;
import com.invoice.model.Cliente;
import com.invoice.model.Empresa;
import com.invoice.model.Pagamento;
import com.invoice.model.Pedido;
import com.invoice.repository.CaixaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CaixaService {

    private final CaixaRepository repository;

    private final EmpresaService empresaService;

    private final ClienteService clienteService;

    private final PedidoService pedidoService;

    public Pagamento salvar(Pagamento pagamento) {
        List<Pagamento> pagamentos = findAllByPedido(pagamento.getPedido().getId());
        Pedido pedido = pedidoService.findById(pagamento.getPedido().getId());
        BigDecimal valorPago = BigDecimal.ZERO;
        for (Pagamento pag: pagamentos) {
            valorPago = valorPago.add(pag.getValorRecebido());
        }
        valorPago = valorPago.add(pagamento.getValorRecebido());
        if(ObjectUtils.isEmpty(pagamento.getPedido().getPagamentoComIva())) {
            valorPago = valorPago.add(valorPago.multiply(pagamento.getCliente().getEmpresa().getPercentualIva()).divide(BigDecimal.valueOf(100), RoundingMode.FLOOR));
        }

        if(valorPago.compareTo(pedido.getValorTotalPedido()) > 0){
            throw new IllegalArgumentException("Os Valores pagos não podem ser superiores ao valor total do pedido");
        } else if(valorPago.compareTo(pedido.getValorTotalPedido()) == 0 || pagamento.getValorRecebido().compareTo(pedido.getValorTotalPedido()) == 0){
            pedido.setEstadoPedido(EstadoPedido.PAGAMENTO_TOTAL_EFETUADO);
        } else {
            pedido.setEstadoPedido(EstadoPedido.PAGAMENTO_INICIAL_EFETUADO);
        }

        pagamento.setEmpresa(empresaService.findById(Long.parseLong(MDC.get("empresa"))));
        pedidoService.update(pedido);
        return repository.save(pagamento);
    }

    public List<Pagamento> findAll() {
        Empresa empresa = empresaService.findById(Long.parseLong(MDC.get("empresa")));
        List<Pagamento> lista = repository.findAllByEmpresa(empresa);
        lista.stream().forEach(pagamento -> pagamento.getPedido().setStatusPedido(pagamento.getPedido().getEstadoPedido().getDescricao()));
        return lista;

    }

    public Pagamento findById(Long id) {
        return repository.findById(id).get();
    }

    public List<Pagamento> findAllByCliente(Long cliente) {
        return repository.findAllByCliente(clienteService.findById(cliente));
    }

    public List<Pagamento> findAllByPedido(Long id) {
        return repository.findAllByPedido(pedidoService.findById(id));
    }
}
