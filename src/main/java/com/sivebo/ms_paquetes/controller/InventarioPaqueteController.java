package com.sivebo.ms_paquetes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivebo.ms_paquetes.dto.request.InventarioPaqueteRequest;
import com.sivebo.ms_paquetes.dto.response.InventarioPaqueteResponse;
import com.sivebo.ms_paquetes.service.InventarioPaqueteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/inventario")
@RequiredArgsConstructor
public class InventarioPaqueteController {

    private final InventarioPaqueteService service;

    @PostMapping("/ingreso")
    public ResponseEntity<InventarioPaqueteResponse> registrarIngreso(@Valid @RequestBody InventarioPaqueteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarIngreso(request));
    }

    @PatchMapping("/{idInv}/salida")
    public ResponseEntity<InventarioPaqueteResponse> registrarSalida(@PathVariable Long idInv) {
        return ResponseEntity.ok(service.registrarSalida(idInv));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioPaqueteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/guia/{idGuia}")
    public ResponseEntity<InventarioPaqueteResponse> obtenerPorGuia(@PathVariable Long idGuia) {
        return ResponseEntity.ok(service.obtenerPorGuia(idGuia));
    }

    @GetMapping("/en-bodega")
    public ResponseEntity<List<InventarioPaqueteResponse>> listarEnBodega() {
        return ResponseEntity.ok(service.listarEnBodega());
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<InventarioPaqueteResponse>> listarPorSucursal(@PathVariable Long idSucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(idSucursal));
    }
}
