package com.sivebo.ms_paquetes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MsPaquetesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPaquetesApplication.class, args);
    }
}