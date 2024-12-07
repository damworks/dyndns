package com.damworks.dyndns.config;

public class DuckDnsConfig {
    private DuckDns duckdns;

    public DuckDns getDuckdns() {
        return duckdns;
    }

    public void setDuckdns(DuckDns duckdns) {
        this.duckdns = duckdns;
    }

    public static class DuckDns {
        private String domain;
        private String token;
        private boolean ipDetectionEnabled;
        private boolean ipv6DetectionEnabled;
        private boolean verbose;
        private boolean clearRecords;

        // Getters and setters
        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isIpDetectionEnabled() {
            return ipDetectionEnabled;
        }

        public void setIpDetectionEnabled(boolean ipDetectionEnabled) {
            this.ipDetectionEnabled = ipDetectionEnabled;
        }

        public boolean isIpv6DetectionEnabled() {
            return ipv6DetectionEnabled;
        }

        public void setIpv6DetectionEnabled(boolean ipv6DetectionEnabled) {
            this.ipv6DetectionEnabled = ipv6DetectionEnabled;
        }

        public boolean isVerbose() {
            return verbose;
        }

        public void setVerbose(boolean verbose) {
            this.verbose = verbose;
        }

        public boolean isClearRecords() {
            return clearRecords;
        }

        public void setClearRecords(boolean clearRecords) {
            this.clearRecords = clearRecords;
        }
    }
}
