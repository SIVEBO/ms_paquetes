package com.sivebo.ms_paquetes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
import com.sivebo.ms_paquetes.service.impl.InventarioPaqueteServiceImpl;

@ExtendWith(MockitoExtension.class)
class MsPaquetesApplicationTests {

    @Mock
    private InventarioPaqueteRepository repository;

    @Mock
    private PaquetesClient paquetesClient;

    @InjectMocks
    private InventarioPaqueteServiceImpl service;

    private InventarioPaquete paquete;
    private InventarioPaqueteRequest request;

    @BeforeEach
    void setUp() {
        paquete = new InventarioPaquete();
        paquete.setIdInv(1L);
        paquete.setIdGuia(100L);
        paquete.setIdSucursal(5L);
        paquete.setFechaIngreso(LocalDate.now());
        paquete.setFechaSalida(null);

        request = new InventarioPaqueteRequest();
        request.setIdGuia(100L);
        request.setIdSucursal(5L);
        request.setFechaIngreso(LocalDate.now());
    }

    @Test
    void registrarIngreso_guiaExiste_creaCorrectamente() {
        when(paquetesClient.verificarGuiaExiste(100L)).thenReturn(true);
        when(repository.save(any(InventarioPaquete.class))).thenReturn(paquete);

        InventarioPaqueteResponse response = service.registrarIngreso(request);

        assertNotNull(response);
        assertEquals(100L, response.getIdGuia());
        assertEquals(5L, response.getIdSucursal());
        verify(paquetesClient).verificarGuiaExiste(100L);
        verify(repository).save(any(InventarioPaquete.class));
    }

    @Test
    void registrarIngreso_guiaNoExiste_lanzaExcepcion() {
        when(paquetesClient.verificarGuiaExiste(100L)).thenReturn(false);

        assertThrows(ReglaNegocioException.class, () -> service.registrarIngreso(request));
        verify(repository, never()).save(any());
    }

    @Test
    void registrarSalida_paqueteExiste_registraFechaSalida() {
        when(repository.findById(1L)).thenReturn(Optional.of(paquete));
        when(repository.save(any(InventarioPaquete.class))).thenReturn(paquete);

        InventarioPaqueteResponse response = service.registrarSalida(1L);

        assertNotNull(response);
        verify(repository).save(paquete);
    }

    @Test
    void registrarSalida_paqueteNoExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.registrarSalida(99L));
    }

    @Test
    void obtenerPorId_existe_retornaPaquete() {
        when(repository.findById(1L)).thenReturn(Optional.of(paquete));

        InventarioPaqueteResponse response = service.obtenerPorId(1L);

        assertEquals(1L, response.getIdInv());
        assertEquals(100L, response.getIdGuia());
    }

    @Test
    void obtenerPorId_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void obtenerPorGuia_existe_retornaPaquete() {
        when(repository.findByIdGuia(100L)).thenReturn(Optional.of(paquete));

        InventarioPaqueteResponse response = service.obtenerPorGuia(100L);

        assertEquals(100L, response.getIdGuia());
    }

    @Test
    void obtenerPorGuia_noExiste_lanzaExcepcion() {
        when(repository.findByIdGuia(999L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorGuia(999L));
    }

    @Test
    void listarEnBodega_retornaListaPaquetesSinSalida() {
        when(repository.findByFechaSalidaIsNull()).thenReturn(List.of(paquete));

        List<InventarioPaqueteResponse> lista = service.listarEnBodega();

        assertEquals(1, lista.size());
        assertNull(lista.get(0).getFechaSalida());
    }

    @Test
    void listarPorSucursal_retornaListaFiltrada() {
        when(repository.findByIdSucursalAndFechaSalidaIsNull(5L)).thenReturn(List.of(paquete));

        List<InventarioPaqueteResponse> lista = service.listarPorSucursal(5L);

        assertEquals(1, lista.size());
        assertEquals(5L, lista.get(0).getIdSucursal());
    }
}
