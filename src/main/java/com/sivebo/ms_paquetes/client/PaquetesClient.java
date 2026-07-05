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
        this.webClient = builder.baseUrl("http://ms-tracking").build();
    }

    public Boolean verificarGuiaExiste(String codigoTracking) {
        log.info("Verificando existencia de guia codigoTracking: {} en ms-tracking", codigoTracking);
        try {
            webClient.get()
                    .uri("/api/v1/guias/buscar?codigoTracking={codigoTracking}", codigoTracking)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (Exception e) {
            log.error("Error al consultar ms-tracking: {}", e.getMessage());
            return false;
        }
    }
}
