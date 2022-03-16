package com.invoice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "item_pedido")
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String produto;

    private String descricaoProduto;

    @ManyToOne
    @JoinColumn(name="pedido_id")
    @JsonBackReference
    private Pedido pedido;

    private BigDecimal altura;

    private BigDecimal largura;

    @Column(name="custo_fabricacao")
    private BigDecimal custoFabricacao;

    @Column(name="valor_venda")
    private BigDecimal valorVenda;

    private BigInteger quantidade;

    @Column(name="total_item")
    private BigDecimal totalItem;

}
