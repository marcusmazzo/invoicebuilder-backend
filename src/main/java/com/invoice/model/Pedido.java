package com.invoice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.invoice.enums.EstadoPedido;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "pedido")
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(name="data_pedido")
    private Date dataPedido;

    @Temporal(TemporalType.DATE)
    @Column(name="data_previsao_entrega")
    private Date dataPrevisaoEntrega;

    @Temporal(TemporalType.DATE)
    @Column(name="data_entrega")
    private Date dataEntrega;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    @JsonManagedReference
    private List<ItemPedido> itens;

    @Column(name="total_itens_pedido")
    private BigInteger totalItensPedido;

    @Column(name="custo_total_pedido")
    private BigDecimal custoTotalPedido;

    @Column(name="valor_total_pedido")
    private BigDecimal valorTotalPedido;

    @OneToMany(mappedBy = "pedido")
    @JsonManagedReference
    private List<Pagamento> pagamentos;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedido;

    private Boolean concluido;

    private Long numero;

    @Column(name="numero_pedido")
    private String numeroPedido;

}
