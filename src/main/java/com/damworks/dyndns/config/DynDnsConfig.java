package com.damworks.dyndns.config;

import java.util.List;

public class DynDnsConfig {
    private Dyndns dyndns;

    public Dyndns getDyndns() {
        return dyndns;
    }

    public void setDyndns(Dyndns dyndns) {
        this.dyndns = dyndns;
    }

    public static class Dyndns {
        private String apiToken;
        private String zoneId;
        private List<Record> records;
        private int checkInterval;

        public String getApiToken() {
            return apiToken;
        }

        public void setApiToken(String apiToken) {
            this.apiToken = apiToken;
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public List<Record> getRecords() {
            return records;
        }

        public void setRecords(List<Record> records) {
            this.records = records;
        }

        public int getCheckInterval() {
            return checkInterval;
        }

        public void setCheckInterval(int checkInterval) {
            this.checkInterval = checkInterval;
        }

        public static class Record {
            private String name;
            private String recordId;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRecordId() {
                return recordId;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }
        }
    }
}
