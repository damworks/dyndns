package com.damworks.dyndns;

import com.damworks.dyndns.config.DuckDnsConfig;
import com.damworks.dyndns.service.DnsUpdaterService;
import com.damworks.dyndns.service.IpFetcherService;
import com.damworks.dyndns.service.IpFileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DynDnsUpdater {
    private static final Logger logger = LoggerFactory.getLogger(DynDnsUpdater.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            logger.info("{} Starting DynDNS update...", timestamp);

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            DuckDnsConfig config = mapper.readValue(new File("config/duckdns.yaml"), DuckDnsConfig.class);

            // Services
            IpFileService ipFileService = new IpFileService();
            IpFetcherService ipFetcherService = new IpFetcherService();

            // Get the current public IP
            String currentIP = ipFetcherService.getCurrentPublicIP();

            // Read the last saved IP
            String lastIP = ipFileService.getLastSavedIP();

            if (currentIP == null || currentIP.isEmpty()) {
                logger.error("Error: Unable to fetch current IP.");
                return;
            }

            // Compare IPs
            if (currentIP.equals(lastIP)) {
                logger.info("No changes -> IP {}", currentIP);
            } else {
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
                    logger.info("New IP -> {} UPDATED", currentIP);
                } catch (Exception e) {
                    logger.error("Error updating DNS records: {}", e.getMessage(), e);
                    e.printStackTrace();
                }

                ipFileService.appendIP(currentIP);
            }

        } catch (Exception e) {
            logger.error("Error during execution: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
