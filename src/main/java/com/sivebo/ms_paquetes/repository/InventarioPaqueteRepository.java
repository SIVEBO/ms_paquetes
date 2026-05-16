package com.sivebo.ms_paquetes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivebo.ms_paquetes.model.entity.InventarioPaquete;

@Repository
public interface InventarioPaqueteRepository extends JpaRepository<InventarioPaquete, Long> {
    List<InventarioPaquete> findByUbicacion_IdUbicacion(Long idUbicacion);
    Optional<InventarioPaquete> findByIdGuiaTracking(Long idGuiaTracking);
    List<InventarioPaquete> findByFechaSalidaBodegaIsNull();
}