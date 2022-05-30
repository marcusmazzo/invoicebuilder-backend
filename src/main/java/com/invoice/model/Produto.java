package com.invoice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "produto")
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String descricao;

    @Column(columnDefinition = "TEXT", name="descricao_completa")
    private String descricaoCompleta;

    @ManyToOne
    @JoinColumn(name = "tipo_produto_id")
    private TipoProduto tipoProduto;

    @Column(name="custo_medio")
    private BigDecimal custoMedio;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(columnDefinition = "TEXT")
    private String garantia;

    private Boolean exigeDimensao;

}
