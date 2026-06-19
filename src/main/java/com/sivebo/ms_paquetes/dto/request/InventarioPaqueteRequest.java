package com.sivebo.ms_paquetes.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventarioPaqueteRequest {

    @NotNull(message = "El id de guia es obligatorio")
    private Long idGuia;

    @NotNull(message = "El id de sucursal es obligatorio")
    private Long idSucursal;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;
}
