package com.example.plan.meal.repository;

import com.example.plan.enums.Day;
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

    @Query("SELECT m FROM Meal m WHERE m.name=:name and m.day=:day and m.type=:type")
    Meal findByName(@Param("name") String name);
    @Query("SELECT m FROM Meal m WHERE m.name=:name and m.day=:day and m.type=:type")
    Meal findByNameDayType(@Param("name") String name, @Param("day") Day day, @Param("type") Type type);
    @Query("SELECT m FROM Meal m WHERE m.name=:name")
    Meal findMealName(String name);

    @Query("SELECT m FROM Meal m WHERE m.type=:type")
    List<Meal> findByType(@Param("type") Type type);

    @Query("SELECT COUNT(mf) > 0 FROM Meal m JOIN m.foods mf WHERE m=:meal AND mf=:food")
    boolean existsMealFoodRelationship(@Param("meal") Meal meal, @Param("food") Food food);

    @Query("SELECT COUNT(m) FROM Meal m")
    int countMeal();

    @Query("SELECT m FROM Meal m WHERE m.calories <= :minCalories AND m.type = :type")
    List<Meal> findMealsByCaloriesLessThanEqual(@Param("minCalories") double minCalories, @Param("type") Type type);

    @Query("SELECT m FROM Meal m WHERE m.calories BETWEEN :minCalories AND :maxCalories AND m.type = :type")
    List<Meal> findMealsByCaloriesBetweenEqual(@Param("minCalories") double minCalories, @Param("maxCalories") double maxCalories, @Param("type") Type type);

    @Query("SELECT m FROM Meal m WHERE m.calories >= :maxCalories AND m.type = :type")
    List<Meal> findMealsByCaloriesMoreThanEqual(@Param("maxCalories") double maxCalories, @Param("type") Type type);


}
