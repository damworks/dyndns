package com.damworks.dyndns;

import com.damworks.dyndns.config.DuckDnsConfig;
import com.damworks.dyndns.service.DnsUpdaterService;
import com.damworks.dyndns.service.IpFetcherService;
import com.damworks.dyndns.service.IpFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;

public class DynDnsUpdater {
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            DuckDnsConfig config = mapper.readValue(new File("config/duckdns.yaml"), DuckDnsConfig.class);

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

                try {
                    // Update the DNS records using DuckDNS API
                    DnsUpdaterService dnsUpdaterService = new DnsUpdaterService(
                            config.getDuckdns().getDomain(),
                            config.getDuckdns().getToken(),
                            config.getDuckdns().isIpDetectionEnabled(),
                            config.getDuckdns().isIpv6DetectionEnabled(),
                            config.getDuckdns().isVerbose(),
                            config.getDuckdns().isClearRecords()
                    );

                    // Pass the current IP to the updater service
                    dnsUpdaterService.updateDuckDns(currentIP, null); // Set IPv6 if needed
                    System.out.println("DNS records updated successfully.");
                } catch (Exception e) {
                    System.err.println("Error updating DNS records: " + e.getMessage());
                    e.printStackTrace();
                }

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
