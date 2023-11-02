package com.example.plan.food.repository;

import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {

    Optional<Food> findByName(String name);

    @Query("SELECT f FROM Food f WHERE f.type=:type")
    List<Food> findByType(Type type);
}
