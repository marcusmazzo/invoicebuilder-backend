package com.invoice.model;


import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "empresa")
@Entity
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String endereco;

    private String telefone;

    private String nipc;

    private String iban;

    @Lob
    private byte[] logotipo;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String informacoes;

    @Column(columnDefinition = "TEXT")
    private String textoRecibo;

    @OneToOne
    private User user;

    @Column(name="prazo_medio_entrega")
    private Long prazoMedioEntrega;

    @Column(name="percentual_lucro")
    private BigDecimal percentualLucro;

    @Column(name="percentual_iva")
    private BigDecimal percentualIva;

    private transient String logotipoBase64;
}
