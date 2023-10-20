package com.example.plan.utils;

import com.example.plan.weeklyPlan.entity.WeeklyPlan;

public class ResponseMessageWithEntity {

    private String message;
    private WeeklyPlan weeklyPlan;

    public ResponseMessageWithEntity(String message, WeeklyPlan weeklyPlan) {
        this.message = message;
        this.weeklyPlan = weeklyPlan;
    }

    public ResponseMessageWithEntity(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public WeeklyPlan getWeeklyPlan() {
        return weeklyPlan;
    }
}
