package com.sivebo.ms_paquetes.controller;

import com.sivebo.ms_paquetes.dto.request.UbicacionBodegaRequest;
import com.sivebo.ms_paquetes.dto.response.UbicacionBodegaResponse;
import com.sivebo.ms_paquetes.service.UbicacionBodegaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ubicaciones")
@RequiredArgsConstructor
public class UbicacionBodegaController {

    private final UbicacionBodegaService service;

    @PostMapping
    public ResponseEntity<UbicacionBodegaResponse> crear(@Valid @RequestBody UbicacionBodegaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<UbicacionBodegaResponse>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionBodegaResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<UbicacionBodegaResponse>> listarPorSucursal(@PathVariable Long idSucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(idSucursal));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionBodegaResponse> actualizar(@PathVariable Long id, @Valid @RequestBody UbicacionBodegaRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}