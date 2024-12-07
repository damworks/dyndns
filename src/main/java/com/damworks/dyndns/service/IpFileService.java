package com.damworks.dyndns.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IpFileService {
    private static final Logger logger = LoggerFactory.getLogger(DnsUpdaterService.class);
    private static final String DATA_DIR = "data";
    private static final String IP_FILE = DATA_DIR + "/ip.txt";

    public IpFileService() {
        ensureDataDirectoryExists();
    }

    private void ensureDataDirectoryExists() {
        Path dataPath = Paths.get(DATA_DIR);
        if (!Files.exists(dataPath)) {
            try {
                Files.createDirectories(dataPath);
            } catch (IOException e) {
                throw new RuntimeException("Error creating the 'data' directory: " + e.getMessage(), e);
            }
        }
    }

    public String getLastSavedIP() {
        try {
            if (Files.exists(Paths.get(IP_FILE))) {
                List<String> lines = Files.readAllLines(Paths.get(IP_FILE));
                if (!lines.isEmpty()) {
                    return lines.get(lines.size() - 1).trim(); // Return the last line
                }
            }
        } catch (IOException e) {
            logger.error("Error reading the IP file: {}", e.getMessage());
        }
        return null; // No saved IP
    }

    public void appendIP(String ip) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(IP_FILE, true))) {
            writer.write(ip);
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error saving the IP: {}", e.getMessage());
        }
    }
}
