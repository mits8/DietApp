package com.example.plan.plan.repository;

import com.example.plan.meal.entity.Meal;
import com.example.plan.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    @Query("SELECT DISTINCT p FROM Plan p "
            + "JOIN FETCH p.meals m")
    List<Plan> findPlansWithMeals();

    @Query("SELECT p FROM Plan p WHERE p.name=:name")
    Plan findByName(@Param("name") String name);

    Plan findPlanById(int id);

    @Query("SELECT p FROM Plan p WHERE p.name=:name")
    Plan findPlanName(String name);

    @Query("SELECT DISTINCT p FROM Plan p "
            + "JOIN  p.meals m "
            + "JOIN  p.customers c "
            + "WHERE c.firstName=:firstName AND c.lastName=:lastName AND p.startDate=:startDate AND p.endDate=:endDate")
    List<Plan> findByCustomerName(@Param("firstName") String firstName, @Param("lastName") String lastName, @Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);

    @Query("SELECT DISTINCT p FROM Plan p "
            + "JOIN  p.meals m "
            + "JOIN  p.customers c "
            + "WHERE c.firstName=:firstName AND c.lastName=:lastName")
    List<Map<String, Object>> findPlanWithRelationships(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT COUNT(pm) > 0 FROM Plan p JOIN p.meals pm WHERE p=:plan and pm=:meal")
    boolean existPlanMealRelationship(@Param("plan") Plan plan, @Param("meal") Meal meal);

}
