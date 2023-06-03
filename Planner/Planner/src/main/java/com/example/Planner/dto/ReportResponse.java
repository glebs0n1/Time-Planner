package com.example.Planner.dto;

import java.time.LocalDate;
import java.util.List;

public class ReportResponse {
    private String message;
    private List<BusyHours> busyHours;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BusyHours> getBusyHours() {
        return busyHours;
    }

    public void setBusyHours(List<BusyHours> busyHours) {
        this.busyHours = busyHours;
    }

    public static class BusyHours {
        private LocalDate date;
        private int busyHours;


        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public int getBusyHours() {
            return busyHours;
        }

        public void setBusyHours(int busyHours) {
            this.busyHours = busyHours;
        }
    }
}
