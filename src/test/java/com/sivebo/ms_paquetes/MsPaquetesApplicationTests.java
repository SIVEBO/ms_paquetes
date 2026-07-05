package com.sivebo.ms_paquetes;

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
import com.sivebo.ms_paquetes.service.InventarioPaqueteService;

@ExtendWith(MockitoExtension.class)
class MsPaquetesApplicationTests {

    @Mock
    private InventarioPaqueteRepository repository;

    @Mock
    private PaquetesClient paquetesClient;

    @InjectMocks
    private InventarioPaqueteService service;

    private InventarioPaquete paquete;
    private InventarioPaqueteRequest request;

    @BeforeEach
    void setUp() {
        paquete = new InventarioPaquete();
        paquete.setIdInv(1L);
        paquete.setCodigoTracking("C32627D89760");
        paquete.setNombreSucursal("Sucursal Norte");
        paquete.setFechaIngreso(LocalDate.now());
        paquete.setFechaSalida(null);

        request = new InventarioPaqueteRequest();
        request.setCodigoTracking("C32627D89760");
        request.setNombreSucursal("Sucursal Norte");
        request.setFechaIngreso(LocalDate.now());
    }

    @Test
    void registrarIngreso_guiaExiste_creaCorrectamente() {
        when(paquetesClient.verificarGuiaExiste("C32627D89760")).thenReturn(true);
        when(repository.save(any(InventarioPaquete.class))).thenReturn(paquete);

        InventarioPaqueteResponse response = service.registrarIngreso(request);

        assertNotNull(response);
        assertEquals("C32627D89760", response.getCodigoTracking());
        assertEquals("Sucursal Norte", response.getNombreSucursal());
        verify(paquetesClient).verificarGuiaExiste("C32627D89760");
        verify(repository).save(any(InventarioPaquete.class));
    }

    @Test
    void registrarIngreso_guiaNoExiste_lanzaExcepcion() {
        when(paquetesClient.verificarGuiaExiste("C32627D89760")).thenReturn(false);

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
        assertEquals("C32627D89760", response.getCodigoTracking());
    }

    @Test
    void obtenerPorId_noExiste_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void obtenerPorGuia_existe_retornaPaquete() {
        when(repository.findByCodigoTracking("C32627D89760")).thenReturn(Optional.of(paquete));

        InventarioPaqueteResponse response = service.obtenerPorGuia("C32627D89760");

        assertEquals("C32627D89760", response.getCodigoTracking());
    }

    @Test
    void obtenerPorGuia_noExiste_lanzaExcepcion() {
        when(repository.findByCodigoTracking("NOEXISTE")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorGuia("NOEXISTE"));
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
        when(repository.findByNombreSucursalAndFechaSalidaIsNull("Sucursal Norte")).thenReturn(List.of(paquete));

        List<InventarioPaqueteResponse> lista = service.listarPorSucursal("Sucursal Norte");

        assertEquals(1, lista.size());
        assertEquals("Sucursal Norte", lista.get(0).getNombreSucursal());
    }
}
