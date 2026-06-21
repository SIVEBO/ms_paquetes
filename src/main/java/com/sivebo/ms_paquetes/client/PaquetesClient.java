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

    public Boolean verificarGuiaExiste(Long idGuia) {
        log.info("Verificando existencia de guia id: {} en ms-guias-despacho", idGuia);
        try {
            webClient.get()
                    .uri("/api/guias/{id}", idGuia)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return true;
        } catch (Exception e) {
            log.error("Error al consultar ms-guias-despacho: {}", e.getMessage());
            return false;
        }
    }
}
