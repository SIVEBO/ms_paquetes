package com.sivebo.ms_paquetes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivebo.ms_paquetes.model.entity.UbicacionBodega;

@Repository
public interface UbicacionBodegaRepository extends JpaRepository<UbicacionBodega, Long> {
    List<UbicacionBodega> findByIdSucursal(Long idSucursal);
}