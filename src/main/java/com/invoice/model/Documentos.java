package com.invoice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.invoice.enums.TipoDocumento;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "documentos")
@Entity
public class Documentos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    private String nomeDocumento;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Lob
    private byte[] documento;

    private transient String contratoBase64;

}
