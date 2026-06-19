package com.sivebo.ms_paquetes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivebo.ms_paquetes.model.entity.InventarioPaquete;

@Repository
public interface InventarioPaqueteRepository extends JpaRepository<InventarioPaquete, Long> {
    Optional<InventarioPaquete> findByIdGuia(Long idGuia);
    List<InventarioPaquete> findByFechaSalidaIsNull();
    List<InventarioPaquete> findByIdSucursalAndFechaSalidaIsNull(Long idSucursal);
}
