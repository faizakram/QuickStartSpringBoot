package com.app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CreateCronExpression {

    private Integer hour;
    private Integer minute;

    public CreateCronExpression(@Value("${app.default.hour}") Integer hour, @Value("${app.default.minute}") Integer minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String generateEveryMinuteCronExpression() {
        // This will run at the start of every minute
        return "0 * * * * ?";
    }


    public String generateHourlyCronExpression() {
        // Validate the input
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }

        // Construct the cron expression
        // This will run at the start of the specified minute past every hour
        return String.format(Locale.US, "0 %d * * * ?", minute);
    }

    public String generateDailyCronExpression() {
        // Validate the input
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }
        // Construct the cron expression
        return String.format(Locale.US, "%d %d * * *", minute, hour);
    }

    public String generateWeeklyCronExpression(int dayOfWeek) {
        // Validate the input
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }
        if (dayOfWeek < 1 || dayOfWeek > 7) {
            throw new IllegalArgumentException("Day of week must be between 1 (Sunday) and 7 (Saturday)");
        }
        // Construct the cron expression with the second field included
        return String.format(Locale.US, "0 %d %d * * %d", minute, hour, dayOfWeek - 1);
    }


    public String generateMonthlyCronExpression(int dayOfMonth) {
        // Validate the input
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23");
        }
        if (minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Minute must be between 0 and 59");
        }
        if (dayOfMonth < 1 || dayOfMonth > 31) {
            throw new IllegalArgumentException("Day of month must be between 1 and 31");
        }

        // Construct the cron expression
        return String.format(Locale.US, "%d %d %d * *", minute, hour, dayOfMonth);
    }
}
