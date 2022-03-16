package com.invoice.model;

import com.invoice.enums.TipoProduto;
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

    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    @Column(name="custo_medio")
    private BigDecimal custoMedio;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

}
