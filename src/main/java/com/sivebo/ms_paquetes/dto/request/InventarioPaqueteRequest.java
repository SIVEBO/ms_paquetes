package com.sivebo.ms_paquetes.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventarioPaqueteRequest {

    @NotNull(message = "El id de guia tracking es obligatorio")
    private Long idGuiaTracking;

    @NotNull(message = "El id de ubicacion es obligatorio")
    private Long idUbicacion;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngresoBodega;

    private LocalDate fechaSalidaBodega;
}