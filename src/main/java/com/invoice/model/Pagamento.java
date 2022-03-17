package com.invoice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "pagamento")
@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="pedido_id")
    @JsonBackReference
    private Pedido pedido;

    @Column(name="valor_pagamento")
    private BigDecimal valorRecebido;

    @Temporal(TemporalType.DATE)
    @Column(name="data_pagamento")
    private Date dataPagamento;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

}
