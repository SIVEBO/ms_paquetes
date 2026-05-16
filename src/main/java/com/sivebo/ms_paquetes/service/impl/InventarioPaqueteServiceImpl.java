package com.sivebo.ms_paquetes.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sivebo.ms_paquetes.dto.request.InventarioPaqueteRequest;
import com.sivebo.ms_paquetes.dto.response.InventarioPaqueteResponse;
import com.sivebo.ms_paquetes.model.entity.InventarioPaquete;
import com.sivebo.ms_paquetes.model.entity.UbicacionBodega;
import com.sivebo.ms_paquetes.repository.InventarioPaqueteRepository;
import com.sivebo.ms_paquetes.repository.UbicacionBodegaRepository;
import com.sivebo.ms_paquetes.service.InventarioPaqueteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioPaqueteServiceImpl implements InventarioPaqueteService {

    private static final Logger log = LoggerFactory.getLogger(InventarioPaqueteServiceImpl.class);
    private final InventarioPaqueteRepository repository;
    private final UbicacionBodegaRepository ubicacionRepository;

    @Override
    public InventarioPaqueteResponse registrarIngreso(InventarioPaqueteRequest request) {
        log.info("Registrando ingreso de paquete guia id: {}", request.getIdGuiaTracking());
        UbicacionBodega ubicacion = ubicacionRepository.findById(request.getIdUbicacion())
                .orElseThrow(() -> new RuntimeException("Ubicacion no encontrada"));
        InventarioPaquete paquete = new InventarioPaquete();
        paquete.setIdGuiaTracking(request.getIdGuiaTracking());
        paquete.setUbicacion(ubicacion);
        paquete.setFechaIngresoBodega(request.getFechaIngresoBodega());
        paquete.setFechaSalidaBodega(null);
        return toResponse(repository.save(paquete));
    }

    @Override
    public InventarioPaqueteResponse registrarSalida(Long idInv) {
        log.info("Registrando salida de paquete id: {}", idInv);
        InventarioPaquete paquete = repository.findById(idInv)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado con id: " + idInv));
        paquete.setFechaSalidaBodega(LocalDate.now());
        return toResponse(repository.save(paquete));
    }

    @Override
    public InventarioPaqueteResponse obtenerPorId(Long id) {
        log.info("Buscando paquete id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado con id: " + id)));
    }

    @Override
    public InventarioPaqueteResponse obtenerPorGuia(Long idGuiaTracking) {
        log.info("Buscando paquete por guia id: {}", idGuiaTracking);
        return toResponse(repository.findByIdGuiaTracking(idGuiaTracking)
                .orElseThrow(() -> new RuntimeException("Paquete no encontrado para guia: " + idGuiaTracking)));
    }

    @Override
    public List<InventarioPaqueteResponse> listarEnBodega() {
        log.info("Listando paquetes en bodega");
        return repository.findByFechaSalidaBodegaIsNull().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<InventarioPaqueteResponse> listarPorUbicacion(Long idUbicacion) {
        log.info("Listando paquetes en ubicacion id: {}", idUbicacion);
        return repository.findByUbicacion_IdUbicacion(idUbicacion).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private InventarioPaqueteResponse toResponse(InventarioPaquete p) {
        InventarioPaqueteResponse r = new InventarioPaqueteResponse();
        r.setIdInv(p.getIdInv());
        r.setIdGuiaTracking(p.getIdGuiaTracking());
        r.setIdUbicacion(p.getUbicacion().getIdUbicacion());
        r.setCodigoEstante(p.getUbicacion().getCodigoEstante());
        r.setFechaIngresoBodega(p.getFechaIngresoBodega());
        r.setFechaSalidaBodega(p.getFechaSalidaBodega());
        return r;
    }
}