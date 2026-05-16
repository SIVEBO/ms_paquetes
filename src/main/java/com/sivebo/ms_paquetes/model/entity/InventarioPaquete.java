package com.sivebo.ms_paquetes.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventario_paquete")
public class InventarioPaquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inv")
    private Long idInv;

    @Column(name = "id_guia_tracking", nullable = false)
    private Long idGuiaTracking;

    @ManyToOne
    @JoinColumn(name = "id_ubicacion", nullable = false)
    private UbicacionBodega ubicacion;

    @Column(name = "fecha_ingreso_bodega", nullable = false)
    private LocalDate fechaIngresoBodega;

    @Column(name = "fecha_salida_bodega")
    private LocalDate fechaSalidaBodega;
}