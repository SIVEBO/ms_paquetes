package com.sivebo.ms_paquetes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivebo.ms_paquetes.client.PaquetesClient;
import com.sivebo.ms_paquetes.dto.request.InventarioPaqueteRequest;
import com.sivebo.ms_paquetes.dto.response.InventarioPaqueteResponse;
import com.sivebo.ms_paquetes.exception.RecursoNoEncontradoException;
import com.sivebo.ms_paquetes.exception.ReglaNegocioException;
import com.sivebo.ms_paquetes.model.entity.InventarioPaquete;
import com.sivebo.ms_paquetes.repository.InventarioPaqueteRepository;

@ExtendWith(MockitoExtension.class)
class InventarioPaqueteServiceTest {

    @Mock
    private InventarioPaqueteRepository repository;

    @Mock
    private PaquetesClient paquetesClient;

    @InjectMocks
    private InventarioPaqueteService service;

    private static final LocalDate HOY = LocalDate.of(2026, 6, 20);
    private static final InventarioPaquete PAQUETE = new InventarioPaquete(1L, 10L, 2L, HOY, null);

    private InventarioPaqueteRequest buildRequest(Long idGuia, Long idSucursal) {
        InventarioPaqueteRequest req = new InventarioPaqueteRequest();
        req.setIdGuia(idGuia);
        req.setIdSucursal(idSucursal);
        req.setFechaIngreso(HOY);
        return req;
    }

    @Test
    void registrarIngresoGuiaExisteGuardaYRetornaDTO() {
        when(paquetesClient.verificarGuiaExiste(10L)).thenReturn(true);
        when(repository.save(any(InventarioPaquete.class))).thenReturn(PAQUETE);

        InventarioPaqueteResponse result = service.registrarIngreso(buildRequest(10L, 2L));

        assertNotNull(result);
        assertEquals(10L, result.getIdGuia());
        assertEquals(2L, result.getIdSucursal());
        verify(repository).save(any(InventarioPaquete.class));
    }

    @Test
    void registrarIngresoGuiaNoExisteLanzaReglaNegocio() {
        when(paquetesClient.verificarGuiaExiste(99L)).thenReturn(false);

        assertThrows(ReglaNegocioException.class, () -> service.registrarIngreso(buildRequest(99L, 1L)));
        verify(repository, never()).save(any());
    }

    @Test
    void registrarSalidaEncontradoAsignaFechaSalida() {
        InventarioPaquete conSalida = new InventarioPaquete(1L, 10L, 2L, HOY, LocalDate.now());

        when(repository.findById(1L)).thenReturn(Optional.of(PAQUETE));
        when(repository.save(any(InventarioPaquete.class))).thenReturn(conSalida);

        InventarioPaqueteResponse result = service.registrarSalida(1L);

        assertNotNull(result.getFechaSalida());
    }

    @Test
    void registrarSalidaNoEncontradoLanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.registrarSalida(99L));
    }

    @Test
    void obtenerPorIdEncontradoRetornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(PAQUETE));

        InventarioPaqueteResponse result = service.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdInv());
    }

    @Test
    void obtenerPorIdNoEncontradoLanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void obtenerPorGuiaEncontradoRetornaDTO() {
        when(repository.findByIdGuia(10L)).thenReturn(Optional.of(PAQUETE));

        InventarioPaqueteResponse result = service.obtenerPorGuia(10L);

        assertNotNull(result);
        assertEquals(10L, result.getIdGuia());
    }

    @Test
    void obtenerPorGuiaNoEncontradoLanzaExcepcion() {
        when(repository.findByIdGuia(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorGuia(99L));
    }

    @Test
    void listarEnBodegaRetornaSoloSinFechaSalida() {
        when(repository.findByFechaSalidaIsNull()).thenReturn(List.of(PAQUETE));

        List<InventarioPaqueteResponse> result = service.listarEnBodega();

        assertEquals(1, result.size());
        assertNull(result.get(0).getFechaSalida());
    }

    @Test
    void listarPorSucursalRetornaListaDeSucursal() {
        when(repository.findByIdSucursalAndFechaSalidaIsNull(2L)).thenReturn(List.of(PAQUETE));

        List<InventarioPaqueteResponse> result = service.listarPorSucursal(2L);

        assertEquals(1, result.size());
        assertEquals(2L, result.get(0).getIdSucursal());
    }
}
