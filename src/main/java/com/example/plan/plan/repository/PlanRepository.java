package com.example.plan.plan.repository;

import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.meal.entity.Meal;
import com.example.plan.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {

    @Query("SELECT DISTINCT p FROM Plan p "
            + "JOIN  p.customers c "
            + "JOIN  p.meals m "
            + "WHERE c.firstname=:firstname AND c.surname=:surname AND c.birthday=:birthday")
    List<Plan> findPlanByCustomerName(String firstname, String surname, LocalDate birthday);

    @Query("SELECT DISTINCT p FROM Plan p "
            + "JOIN FETCH p.meals m")
    List<Plan> findPlansWithMeals();

    @Query("SELECT p FROM Plan p WHERE p.name=:name")
    Plan findByName(@Param("name") String name);

    @Query("SELECT p FROM Plan p WHERE p.name=:name and p.startDate=:startDate and p.endDate=:endDate")
    Plan findByNameAndDates(@Param("name") String name, LocalDate startDate, LocalDate endDate);
    Plan findPlanById(int id);

    @Query("SELECT p FROM Plan p WHERE p.name=:name")
    Plan findPlanName(String name);

    @Query("SELECT DISTINCT p FROM Plan p "
            + "JOIN  p.meals m "
            + "JOIN  p.customers c "
            + "WHERE c.firstname=:firstname AND c.surname=:surname AND p.startDate=:startDate AND p.endDate=:endDate")
    List<Plan> findByCustomerNameAndDates(@Param("firstname") String firstname, @Param("surname") String surname, @Param("startDate")LocalDate startDate, @Param("endDate")LocalDate endDate);

    @Query("SELECT DISTINCT p FROM Plan p "
            + "JOIN  p.meals m "
            + "JOIN  p.customers c "
            + "WHERE c.firstname=:firstname AND c.surname=:surname")
    List<Map<String, Object>> findPlanWithRelationships(@Param("firstname") String firstname, @Param("surname") String surname);

    @Query("SELECT COUNT(pm) > 0 FROM Plan p JOIN p.meals pm WHERE p=:plan and pm=:meal")
    boolean existPlanMealRelationship(@Param("plan") Plan plan, @Param("meal") Meal meal);

    @Query("SELECT COUNT(p) FROM Plan p")
    int countPlans();

    @Query("SELECT COUNT(pc) > 0 FROM Plan m JOIN m.customers pc WHERE m=:plan AND pc=:customer")
    boolean existsCustomerPlanRelationship(@Param("plan") Plan plan, @Param("customer") DietCustomer customer);

    @Query("SELECT p.name as name, p.startDate as startDate, p.endDate as endDate," +
            "       m.day as day, m.type as type, m.calories as calories, m.name as mealName," +
            "        CONCAT(c.firstname, ' ', c.surname) as customerFullName " +
            "FROM Plan p " +
            "JOIN p.customers c " +
            "JOIN p.meals m " +
            "WHERE c.firstname = :firstname and c.surname = :surname")
    List<Map<String, Object>> displayWholePlan(@Param("firstname") String firstname, @Param("surname") String surname);
}
