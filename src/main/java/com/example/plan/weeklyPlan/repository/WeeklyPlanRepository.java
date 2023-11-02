package com.example.plan.weeklyPlan.repository;

import com.example.plan.weeklyPlan.entity.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Integer> {

    @Query("SELECT w FROM WeeklyPlan w WHERE w.name=:name")
    WeeklyPlan findByName(String name);

    WeeklyPlan findWeeklyPlanById(int id);
}
