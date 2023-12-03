package com.example.plan.meal.repository;

import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.name=:name")
    Meal findByName(@Param("name") String name);
    @Query("SELECT m FROM Meal m WHERE m.name=:name")
    Meal findMealName(String name);

    @Query("SELECT m FROM Meal m WHERE m.type=:type")
    List<Meal> findByType(@Param("type") Type type);

    @Query("SELECT COUNT(mf) > 0 FROM Meal m JOIN m.foods mf WHERE m=:meal AND mf=:food")
    boolean existsMealFoodRelationship(@Param("meal") Meal meal, @Param("food") Food food);

    @Query("SELECT COUNT(m) FROM Meal m")
    int countMeal();
}
