package com.example.plan.food.repository;

import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {


    @Query("SELECT p FROM Plan p WHERE p.name=:name")
    Food findByName(@Param("name") String name);

    @Query("SELECT f FROM Food f WHERE f.type=:type")
    List<Food> findByType(Type type);

    @Query("SELECT f FROM Food f WHERE f.name=:name")
    Food findFoodName(String name);
}
