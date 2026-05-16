package com.sivebo.ms_paquetes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UbicacionBodegaRequest {

    @NotNull(message = "El id de sucursal es obligatorio")
    private Long idSucursal;

    @NotBlank(message = "El código de estante es obligatorio")
    private String codigoEstante;
}