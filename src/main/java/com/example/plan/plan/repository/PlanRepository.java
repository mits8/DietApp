package com.example.plan.plan.repository;

import com.example.plan.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    @Query("SELECT w FROM Plan w WHERE w.name=:name")
    Plan findByName(String name);

    Plan findPlanById(int id);
}
