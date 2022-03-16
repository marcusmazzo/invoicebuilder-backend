package com.invoice.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "cliente")
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String contato;

    private String endereco;

    private String docIdentificacao;

    private String email;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}
