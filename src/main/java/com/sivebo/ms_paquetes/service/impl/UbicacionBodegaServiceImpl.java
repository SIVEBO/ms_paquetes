package com.sivebo.ms_paquetes.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sivebo.ms_paquetes.dto.request.UbicacionBodegaRequest;
import com.sivebo.ms_paquetes.dto.response.UbicacionBodegaResponse;
import com.sivebo.ms_paquetes.model.entity.UbicacionBodega;
import com.sivebo.ms_paquetes.repository.UbicacionBodegaRepository;
import com.sivebo.ms_paquetes.service.UbicacionBodegaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UbicacionBodegaServiceImpl implements UbicacionBodegaService {

    private static final Logger log = LoggerFactory.getLogger(UbicacionBodegaServiceImpl.class);
    private final UbicacionBodegaRepository repository;

    @Override
    public UbicacionBodegaResponse crear(UbicacionBodegaRequest request) {
        log.info("Creando ubicacion: {}", request.getCodigoEstante());
        UbicacionBodega ubicacion = new UbicacionBodega();
        ubicacion.setIdSucursal(request.getIdSucursal());
        ubicacion.setCodigoEstante(request.getCodigoEstante());
        return toResponse(repository.save(ubicacion));
    }

    @Override
    public UbicacionBodegaResponse obtenerPorId(Long id) {
        log.info("Buscando ubicacion id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con id: " + id)));
    }

    @Override
    public List<UbicacionBodegaResponse> listarTodas() {
        log.info("Listando todas las ubicaciones");
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<UbicacionBodegaResponse> listarPorSucursal(Long idSucursal) {
        log.info("Listando ubicaciones de sucursal id: {}", idSucursal);
        return repository.findByIdSucursal(idSucursal).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public UbicacionBodegaResponse actualizar(Long id, UbicacionBodegaRequest request) {
        log.info("Actualizando ubicacion id: {}", id);
        UbicacionBodega ubicacion = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada con id: " + id));
        ubicacion.setIdSucursal(request.getIdSucursal());
        ubicacion.setCodigoEstante(request.getCodigoEstante());
        return toResponse(repository.save(ubicacion));
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando ubicacion id: {}", id);
        repository.deleteById(id);
    }

    private UbicacionBodegaResponse toResponse(UbicacionBodega u) {
        UbicacionBodegaResponse r = new UbicacionBodegaResponse();
        r.setIdUbicacion(u.getIdUbicacion());
        r.setIdSucursal(u.getIdSucursal());
        r.setCodigoEstante(u.getCodigoEstante());
        return r;
    }
}