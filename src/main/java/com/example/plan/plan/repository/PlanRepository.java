package com.example.plan.plan.repository;

import com.example.plan.meal.entity.Meal;
import com.example.plan.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT p.name, p.duration, CONCAT(c.firstName, ' ', c.lastName) AS fullName, m.name, m.quantity, m.type, f.name " +
            "FROM Plan p " +
            "JOIN p.customers c " +
            "JOIN p.meals m " +
            "JOIN m.foods f " +
            "WHERE c.firstName =:firstName and c.lastName=:lastName")
    List<Map<String, Object>> findPlanDetailsByCustomerFullName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT COUNT(pm) > 0 FROM Plan p JOIN p.meals pm WHERE p=:plan and pm=:meal")
    boolean existPlanMealRelationship(@Param("plan") Plan plan, @Param("meal") Meal meal);

}
