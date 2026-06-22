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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Inventario de Paquetes", description = "Control de paquetes en bodega por sucursal")
@RestController
@RequestMapping("api/v1/inventario")
@RequiredArgsConstructor
public class InventarioPaqueteController {

    private final InventarioPaqueteService service;

    @Operation(summary = "Registrar ingreso de paquete", description = "Asocia un paquete a su guía y lo ingresa a bodega")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Ingreso registrado"),
        @ApiResponse(responseCode = "400", description = "Guía de despacho no existe en el sistema")
    })
    @PostMapping("/ingreso")
    public ResponseEntity<InventarioPaqueteResponse> registrarIngreso(@Valid @RequestBody InventarioPaqueteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarIngreso(request));
    }

    @Operation(summary = "Registrar salida de paquete", description = "marca la fecha de egreso del paquete de bodega")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Salida registrada"),
        @ApiResponse(responseCode = "404", description = "Registro de inventario no encontrado")
    })
    @PatchMapping("/{idInv}/salida")
    public ResponseEntity<InventarioPaqueteResponse> registrarSalida(@PathVariable Long idInv) {
        return ResponseEntity.ok(service.registrarSalida(idInv));
    }

    @Operation(summary = "Obtener registro por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioPaqueteResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Obtener inventario por guía")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "No hay inventario para esa guía")
    })
    @GetMapping("/guia/{idGuia}")
    public ResponseEntity<InventarioPaqueteResponse> obtenerPorGuia(@PathVariable Long idGuia) {
        return ResponseEntity.ok(service.obtenerPorGuia(idGuia));
    }

    @Operation(summary = "Listar paquetes en bodega", description = "muestra paquetes sin fecha de salida")
    @ApiResponse(responseCode = "200", description = "Lista de paquetes en bodega")
    @GetMapping("/en-bodega")
    public ResponseEntity<List<InventarioPaqueteResponse>> listarEnBodega() {
        return ResponseEntity.ok(service.listarEnBodega());
    }

    @Operation(summary = "Listar paquetes en bodega por sucursal")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por sucursal")
    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<InventarioPaqueteResponse>> listarPorSucursal(@PathVariable Long idSucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(idSucursal));
    }
}
