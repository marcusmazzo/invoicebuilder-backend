package com.invoice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusVisita {

    AGENDADO("Agendado"),
    EM_CURSO("Visita em curso"),
    VISITADO("Visita efetuada"),
    CANCELADO("Visita cancelada");

    private final String descricao;
}
