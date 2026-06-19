package com.sivebo.ms_paquetes.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sivebo.ms_paquetes.client.PaquetesClient;
import com.sivebo.ms_paquetes.dto.request.InventarioPaqueteRequest;
import com.sivebo.ms_paquetes.dto.response.InventarioPaqueteResponse;
import com.sivebo.ms_paquetes.exception.RecursoNoEncontradoException;
import com.sivebo.ms_paquetes.exception.ReglaNegocioException;
import com.sivebo.ms_paquetes.model.entity.InventarioPaquete;
import com.sivebo.ms_paquetes.repository.InventarioPaqueteRepository;
import com.sivebo.ms_paquetes.service.InventarioPaqueteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioPaqueteServiceImpl implements InventarioPaqueteService {

    private static final Logger log = LoggerFactory.getLogger(InventarioPaqueteServiceImpl.class);
    private final InventarioPaqueteRepository repository;
    private final PaquetesClient paquetesClient;

    @Override
    public InventarioPaqueteResponse registrarIngreso(InventarioPaqueteRequest request) {
        log.info("Registrando ingreso de paquete guia id: {}", request.getIdGuia());

        Boolean guiaExiste = paquetesClient.verificarGuiaExiste(request.getIdGuia());
        if (!guiaExiste) {
            throw new ReglaNegocioException("La guia de despacho no existe en el sistema");
        }

        InventarioPaquete paquete = new InventarioPaquete();
        paquete.setIdGuia(request.getIdGuia());
        paquete.setIdSucursal(request.getIdSucursal());
        paquete.setFechaIngreso(request.getFechaIngreso());
        return toResponse(repository.save(paquete));
    }

    @Override
    public InventarioPaqueteResponse registrarSalida(Long idInv) {
        log.info("Registrando salida de paquete id: {}", idInv);
        InventarioPaquete paquete = repository.findById(idInv)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paquete no encontrado con id: " + idInv));
        paquete.setFechaSalida(LocalDate.now());
        return toResponse(repository.save(paquete));
    }

    @Override
    public InventarioPaqueteResponse obtenerPorId(Long id) {
        log.info("Buscando paquete id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paquete no encontrado con id: " + id)));
    }

    @Override
    public InventarioPaqueteResponse obtenerPorGuia(Long idGuia) {
        log.info("Buscando paquete por guia id: {}", idGuia);
        return toResponse(repository.findByIdGuia(idGuia)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paquete no encontrado para guia: " + idGuia)));
    }

    @Override
    public List<InventarioPaqueteResponse> listarEnBodega() {
        log.info("Listando paquetes en bodega");
        return repository.findByFechaSalidaIsNull().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<InventarioPaqueteResponse> listarPorSucursal(Long idSucursal) {
        log.info("Listando paquetes en bodega de sucursal id: {}", idSucursal);
        return repository.findByIdSucursalAndFechaSalidaIsNull(idSucursal).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private InventarioPaqueteResponse toResponse(InventarioPaquete p) {
        InventarioPaqueteResponse r = new InventarioPaqueteResponse();
        r.setIdInv(p.getIdInv());
        r.setIdGuia(p.getIdGuia());
        r.setIdSucursal(p.getIdSucursal());
        r.setFechaIngreso(p.getFechaIngreso());
        r.setFechaSalida(p.getFechaSalida());
        return r;
    }
}
