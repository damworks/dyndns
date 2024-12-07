package com.damworks.dyndns.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IpFetcherService {
    public String getCurrentPublicIP() throws Exception {
        URL url = new URL("https://api.ipify.org");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return reader.readLine();
        }
    }
}
