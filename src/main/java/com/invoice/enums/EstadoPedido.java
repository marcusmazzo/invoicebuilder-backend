package com.invoice.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EstadoPedido {

    PEDIDO_CRIADO ("Pedido Criado"),
    ORCAMENTO_CRIADO( "Orçamento Criado"),
    ORCAMENTO_ENVIADO( "Orçamento Enviado"),
    ORCAMENTO_ACEITO ("Orçamento Aceito"),
    PAGAMENTO_INICIAL_EFETUADO ("Pagamento Inicial efetuado"),
    PAGAMENTO_TOTAL_EFETUADO ("Pagamento total efetudo"),
    CONCLUIDO("Pedido concluído"),
    CANCELADO("Pedido Cancelado");

    private String descricao;
}
