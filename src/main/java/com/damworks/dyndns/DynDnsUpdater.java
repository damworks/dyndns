package com.damworks.dyndns;

import com.damworks.dyndns.config.DynDnsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;

public class DynDnsUpdater {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            DynDnsConfig config = mapper.readValue(new File("config/application.yaml"), DynDnsConfig.class);

            System.out.println("Configurazione caricata:");
            System.out.println("API Token: " + config.getDyndns().getApiToken());
            System.out.println("Zona ID: " + config.getDyndns().getZoneId());
            System.out.println("Intervallo di controllo: " + config.getDyndns().getCheckInterval() + " minuti");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
