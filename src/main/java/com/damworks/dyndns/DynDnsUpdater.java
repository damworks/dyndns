package com.damworks.dyndns;

import com.damworks.dyndns.config.DynDnsConfig;
import com.damworks.dyndns.service.IpFetcherService;
import com.damworks.dyndns.service.IpFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;

public class DynDnsUpdater {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            DynDnsConfig config = mapper.readValue(new File("config/application.yaml"), DynDnsConfig.class);

            System.out.println("Configuration loaded:");
            System.out.println("API Token: " + config.getDyndns().getApiToken());
            System.out.println("Zone ID: " + config.getDyndns().getZoneId());
            System.out.println("Check interval: " + config.getDyndns().getCheckInterval() + " minutes");

            // Services
            IpFileService ipFileService = new IpFileService();
            IpFetcherService ipFetcherService = new IpFetcherService();

            // Get the current public IP
            String currentIP = ipFetcherService.getCurrentPublicIP();
            System.out.println("Current public IP: " + currentIP);

            // Read the last saved IP
            String lastIP = ipFileService.getLastSavedIP();
            System.out.println("Last saved IP: " + (lastIP != null ? lastIP : "None"));

            // Compare IPs
            if (!currentIP.equals(lastIP)) {
                System.out.println("The IP address has changed, update required.");

                // Save the new IP
                ipFileService.appendIP(currentIP);
            } else {
                System.out.println("The IP address has not changed, no update needed.");
            }

        } catch (Exception e) {
            System.err.println("Error during execution: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
