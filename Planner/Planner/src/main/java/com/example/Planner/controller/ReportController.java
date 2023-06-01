package com.example.Planner.controller;

import com.example.Planner.dto.ReportRequest;
import com.example.Planner.dto.ReportResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@RestController
public class ReportController {

    @PostMapping("/report")
    public ReportResponse calculateReport(@RequestBody ReportRequest request) {
        ReportResponse response = new ReportResponse();
        List<ReportResponse.BusyHours> busyHoursList = new ArrayList<>();

        LocalDate currentDate = LocalDate.now();
        LocalDate deadline = LocalDate.parse("2023-07-01"); // Set the desired end date

        long totalAvailableDays = ChronoUnit.DAYS.between(currentDate, deadline);

        // Subtract non-working days (weekends and holidays)
        totalAvailableDays -= calculateNonWorkingDays(currentDate, deadline, request.getVacationDays());

        // Subtract time for sleeping, commuting, and vacation days
        long totalAvailableHours = totalAvailableDays * request.getHoursPerDay() * 24; // Multiply by 24 to convert days to hours
        totalAvailableHours -= calculateSleepingHours(totalAvailableDays);
        totalAvailableHours -= (calculateCommuteTimePerDay(request.getCommuteTime()) + request.getVacationDays()) * totalAvailableDays;

        // Calculate available time per day
        int availableTimePerDay = (int) Math.floor(totalAvailableHours / totalAvailableDays);

        // Determine the completion status and set response message
        String completionStatus = determineCompletionStatus(totalAvailableHours, 80 * 24); // Update the amountOfWork in hours
        response.setMessage(completionStatus);

        // Populate the busy hours list
        for (int i = 0; i < totalAvailableDays; i++) {
            LocalDate date = currentDate.plusDays(i);
            int busyHours = calculateBusyHoursForDate(date);

            // Adjust busy hours for weekends
            if (isWeekend(date)) {
                busyHours = Math.min(busyHours, availableTimePerDay);
            }

            ReportResponse.BusyHours busyHoursObj = new ReportResponse.BusyHours();
            busyHoursObj.setDate(date);
            busyHoursObj.setBusyHours(busyHours);

            busyHoursList.add(busyHoursObj);
        }

        response.setBusyHours(busyHoursList);

        return response;
    }

    private int calculateBusyHoursForDate(LocalDate date) {
        // Logic to calculate the busy hours for a specific date the method generate a random number between 0 and 8 (inclusive) as the busy hours
        Random random = new Random();
        return random.nextInt(9);
    }

    private int calculateNonWorkingDays(LocalDate startDate, LocalDate endDate, int vacationDays) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        int nonWorkingDays = 0;

        for (int i = 0; i <= totalDays; i++) {
            LocalDate currentDate = startDate.plusDays(i);

            if (isWeekend(currentDate) || isVacationDay(currentDate, vacationDays)) {
                nonWorkingDays++;
            }
        }

        return nonWorkingDays;
    }

    private int calculateSleepingHours(long totalAvailableDays) {
        // Calculate total sleeping hours for the given number of days
        return (int) (8 * totalAvailableDays);
    }

    private int calculateCommuteTimePerDay(int commuteTime) {
        // Calculate commute time per day
        return commuteTime * 5; // Assuming 5 working days per week
    }

    private boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    private boolean isVacationDay(LocalDate date, int vacationDays) {
        // Define the specific vacation days
        LocalDate vacationDay1 = LocalDate.of(2023, 6, 15);
        LocalDate vacationDay2 = LocalDate.of(2023, 6, 30);
        return date.equals(vacationDay1) || date.equals(vacationDay2);
    }

    private String determineCompletionStatus(long totalAvailableHours, int amountOfWork) {
        if (totalAvailableHours >= amountOfWork) {
            return "You can definitely complete the task on time!";
        } else {
            return "Although it's challenging, you can still do it if you manage your time efficiently!";
        }
    }
}
