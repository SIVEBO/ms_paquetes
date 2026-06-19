package com.sivebo.ms_paquetes.service;

import java.util.List;

import com.sivebo.ms_paquetes.dto.request.InventarioPaqueteRequest;
import com.sivebo.ms_paquetes.dto.response.InventarioPaqueteResponse;

public interface InventarioPaqueteService {
    InventarioPaqueteResponse registrarIngreso(InventarioPaqueteRequest request);
    InventarioPaqueteResponse registrarSalida(Long idInv);
    InventarioPaqueteResponse obtenerPorId(Long id);
    InventarioPaqueteResponse obtenerPorGuia(Long idGuia);
    List<InventarioPaqueteResponse> listarEnBodega();
    List<InventarioPaqueteResponse> listarPorSucursal(Long idSucursal);
}
