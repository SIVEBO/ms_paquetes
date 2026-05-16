package com.sivebo.ms_paquetes.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class InventarioPaqueteResponse {
    private Long idInv;
    private Long idGuiaTracking;
    private Long idUbicacion;
    private String codigoEstante;
    private LocalDate fechaIngresoBodega;
    private LocalDate fechaSalidaBodega;
}