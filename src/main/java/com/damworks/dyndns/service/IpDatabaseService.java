package com.damworks.dyndns.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IpDatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(IpDatabaseService.class);

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public IpDatabaseService(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public String getLastSavedIP() {
        String query = "SELECT ip_address FROM ip_logs ORDER BY timestamp DESC LIMIT 1";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString("ip_address");
            }
        } catch (Exception e) {
            logger.error("Error fetching the last saved IP from the database: {}", e.getMessage(), e);
        }

        return null;
    }

    public void saveIP(String ip) {
        String query = "INSERT INTO ip_logs (ip_address) VALUES (?)";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, ip);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.error("Error saving IP to the database: {}", e.getMessage(), e);
        }
    }
}
