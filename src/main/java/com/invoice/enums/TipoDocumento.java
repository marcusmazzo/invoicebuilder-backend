package com.invoice.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoDocumento {

    ORCAMENTO("Orçamento"), IMAGEM("Imagem");

    private String descicao;

}
