package com.sivebo.ms_paquetes.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PaquetesClient {

    private static final Logger log = LoggerFactory.getLogger(PaquetesClient.class);
    private final WebClient webClient;

    public PaquetesClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8082").build();
    }

    public Boolean verificarStockEmbalaje(Long idArt, Long idSucursal, Integer cantidad) {
        log.info("Verificando stock de embalaje para artículo id: {}", idArt);
        try {
            return webClient.get()
                    .uri("/api/stock/verificar?idArt={idArt}&idSucursal={idSucursal}&cantidadRequerida={cantidad}",
                            idArt, idSucursal, cantidad)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            log.error("Error al consultar ms-embalaje: {}", e.getMessage());
            return false;
        }
    }
}