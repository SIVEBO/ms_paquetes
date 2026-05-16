package com.sivebo.ms_paquetes.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ubicacion_bodega")
public class UbicacionBodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubicacion")
    private Long idUbicacion;

    @Column(name = "id_sucursal", nullable = false)
    private Long idSucursal;

    @Column(name = "codigo_estante", nullable = false, length = 50)
    private String codigoEstante;
}