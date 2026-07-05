package com.sivebo.ms_paquetes.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sivebo.ms_paquetes.client.PaquetesClient;
import com.sivebo.ms_paquetes.dto.request.InventarioPaqueteRequest;
import com.sivebo.ms_paquetes.dto.response.InventarioPaqueteResponse;
import com.sivebo.ms_paquetes.exception.RecursoNoEncontradoException;
import com.sivebo.ms_paquetes.exception.ReglaNegocioException;
import com.sivebo.ms_paquetes.model.entity.InventarioPaquete;
import com.sivebo.ms_paquetes.repository.InventarioPaqueteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioPaqueteService{

    private final InventarioPaqueteRepository repository;
    private final PaquetesClient paquetesClient;

    
    public InventarioPaqueteResponse registrarIngreso(InventarioPaqueteRequest request) {
        log.info("Registrando ingreso de paquete guia codigoTracking: {}", request.getCodigoTracking());

        Boolean guiaExiste = paquetesClient.verificarGuiaExiste(request.getCodigoTracking());
        if (!guiaExiste) {
            throw new ReglaNegocioException("La guia de despacho no existe en el sistema");
        }

        InventarioPaquete paquete = new InventarioPaquete();
        paquete.setCodigoTracking(request.getCodigoTracking());
        paquete.setNombreSucursal(request.getNombreSucursal());
        paquete.setFechaIngreso(request.getFechaIngreso());
        return toResponse(repository.save(paquete));
    }

    
    public InventarioPaqueteResponse registrarSalida(Long idInv) {
        log.info("Registrando salida de paquete id: {}", idInv);
        InventarioPaquete paquete = repository.findById(idInv)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paquete no encontrado con id: " + idInv));
        paquete.setFechaSalida(LocalDate.now());
        return toResponse(repository.save(paquete));
    }

    
    public InventarioPaqueteResponse obtenerPorId(Long id) {
        log.info("Buscando paquete id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paquete no encontrado con id: " + id)));
    }

    
    public InventarioPaqueteResponse obtenerPorGuia(String codigoTracking) {
        log.info("Buscando paquete por guia codigoTracking: {}", codigoTracking);
        return toResponse(repository.findByCodigoTracking(codigoTracking)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paquete no encontrado para guia: " + codigoTracking)));
    }

    
    public List<InventarioPaqueteResponse> listarEnBodega() {
        log.info("Listando paquetes en bodega");
        return repository.findByFechaSalidaIsNull().stream().map(this::toResponse).collect(Collectors.toList());
    }

    
    public List<InventarioPaqueteResponse> listarPorSucursal(String nombreSucursal) {
        log.info("Listando paquetes en bodega de sucursal: {}", nombreSucursal);
        return repository.findByNombreSucursalAndFechaSalidaIsNull(nombreSucursal).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private InventarioPaqueteResponse toResponse(InventarioPaquete p) {
        InventarioPaqueteResponse r = new InventarioPaqueteResponse();
        r.setIdInv(p.getIdInv());
        r.setCodigoTracking(p.getCodigoTracking());
        r.setNombreSucursal(p.getNombreSucursal());
        r.setFechaIngreso(p.getFechaIngreso());
        r.setFechaSalida(p.getFechaSalida());
        return r;
    }
}
