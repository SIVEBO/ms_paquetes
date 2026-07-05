package com.sivebo.ms_paquetes.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioPaqueteResponse {
    private Long idInv;
    private String codigoTracking;
    private String nombreSucursal;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;
}
