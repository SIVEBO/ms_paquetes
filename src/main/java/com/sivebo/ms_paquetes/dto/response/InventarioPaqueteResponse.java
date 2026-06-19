package com.sivebo.ms_paquetes.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InventarioPaqueteResponse {
    private Long idInv;
    private Long idGuia;
    private Long idSucursal;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
}
