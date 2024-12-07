package com.damworks.dyndns.service;

import com.damworks.dyndns.config.CloudflareDnsConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DnsUpdaterService {
    private final String domain;
    private final String token;
    private final boolean ipDetectionEnabled;
    private final boolean ipv6DetectionEnabled;
    private final boolean verbose;
    private final boolean clearRecords;

    public DnsUpdaterService(String domain, String token, boolean ipDetectionEnabled, boolean ipv6DetectionEnabled, boolean verbose, boolean clearRecords) {
        this.domain = domain;
        this.token = token;
        this.ipDetectionEnabled = ipDetectionEnabled;
        this.ipv6DetectionEnabled = ipv6DetectionEnabled;
        this.verbose = verbose;
        this.clearRecords = clearRecords;
    }

    public void updateDuckDns(String ipv4, String ipv6) throws Exception {
        StringBuilder apiUrl = new StringBuilder(String.format(
                "https://www.duckdns.org/update?domains=%s&token=%s",
                domain, token
        ));

        if (clearRecords) {
            apiUrl.append("&clear=true");
        } else {
            if (ipDetectionEnabled && ipv4 == null) {
                // Rilevamento automatico dell'IP IPv4
            } else if (ipv4 != null) {
                apiUrl.append("&ip=").append(ipv4);
            }

            if (ipv6DetectionEnabled && ipv6 != null) {
                apiUrl.append("&ipv6=").append(ipv6);
            }
        }
        if (verbose) {
            apiUrl.append("&verbose=true");
        }

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl.toString()).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String response = reader.readLine();
            if (response != null && response.startsWith("OK")) {
                System.out.println("DuckDNS update successful. Response: " + response);
            } else {
                throw new Exception("DuckDNS update failed. Response: " + response);
            }
        }
    }

    public void updateDnsRecord(String apiToken, String zoneId, CloudflareDnsConfig.Dyndns.Record record, String newIP) throws Exception {
        String apiUrl = String.format("https://api.cloudflare.com/client/v4/zones/%s/dns_records/%s", zoneId, record.getRecordId());
        String payload = String.format("{\"type\":\"A\",\"name\":\"%s\",\"content\":\"%s\",\"ttl\":1,\"proxied\":false}", record.getName(), newIP);

        HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("Authorization", "Bearer " + apiToken);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(payload.getBytes());
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            System.out.println("DNS Record updated: " + record.getName() + " -> " + newIP);
        } else {
            System.err.println("Error updating DNS record. Response code: " + responseCode);
        }
    }
}