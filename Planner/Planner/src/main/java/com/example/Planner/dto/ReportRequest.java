package com.example.Planner.dto;

public class ReportRequest {
    private int hoursPerDay;
    private int commuteTime;
    private int vacationDays;

    public int getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(int hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public int getCommuteTime() {
        return commuteTime;
    }

    public void setCommuteTime(int commuteTime) {
        this.commuteTime = commuteTime;
    }

    public int getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(int vacationDays) {
        this.vacationDays = vacationDays;
    }
}
