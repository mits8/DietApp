package com.example.plan.meal.repository;

import com.example.plan.enums.Type;
import com.example.plan.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.name=:name")
    Meal findByName(@Param("name") String name);

    @Query("SELECT m FROM Meal m WHERE m.type=:type")
    List<Meal> findByType(@Param("type") Type type);

    Meal findMealById(int id);
}
