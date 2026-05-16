package com.sivebo.ms_paquetes.service;

import java.util.List;

import com.sivebo.ms_paquetes.dto.request.UbicacionBodegaRequest;
import com.sivebo.ms_paquetes.dto.response.UbicacionBodegaResponse;

public interface UbicacionBodegaService {
    UbicacionBodegaResponse crear(UbicacionBodegaRequest request);
    UbicacionBodegaResponse obtenerPorId(Long id);
    List<UbicacionBodegaResponse> listarTodas();
    List<UbicacionBodegaResponse> listarPorSucursal(Long idSucursal);
    UbicacionBodegaResponse actualizar(Long id, UbicacionBodegaRequest request);
    void eliminar(Long id);
}