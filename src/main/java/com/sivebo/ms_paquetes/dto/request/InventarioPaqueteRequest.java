package com.sivebo.ms_paquetes.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioPaqueteRequest {

    @NotBlank(message = "El código de tracking es obligatorio")
    private String codigoTracking;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombreSucursal;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    private LocalDate fechaIngreso;
}
