package com.example.plan.plan.repository;

import com.example.plan.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Integer> {

    @Query("SELECT w FROM Plan w WHERE w.name=:name")
    Plan findByName(@Param("name") String name);

    Plan findPlanById(int id);


    @Query("SELECT p.name, p.duration, CONCAT(c.firstName, ' ', c.lastName) AS fullName, m.name, m.quantity, m.type, f.name " +
            "FROM Plan p " +
            "JOIN p.customers c " +
            "JOIN p.meals m " +
            "JOIN m.foods f " +
            "WHERE c.firstName =:firstName and c.lastName=:lastName")
    List<Object> findPlanDetailsByCustomerFirstName(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
