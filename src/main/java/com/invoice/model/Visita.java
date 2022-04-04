package com.invoice.model;

import com.invoice.enums.StatusVisita;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "visita")
@Entity
public class Visita {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cliente_id")
    private Cliente cliente;

    @Temporal(TemporalType.DATE)
    private Date dataVisita;

    @Enumerated(EnumType.STRING)
    private StatusVisita statusVisita;

    private transient String descricaoStatusVisita;

}
