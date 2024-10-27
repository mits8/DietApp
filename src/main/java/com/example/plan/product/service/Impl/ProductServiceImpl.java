package com.example.plan.product.service.Impl;

import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.product.entity.Product;
import com.example.plan.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private PlanRepository planRepository;

    @Override
    public Product count() {
        Product product = new Product();
        int countCustomer = customerRepository.countCustomer();
        int countFoods = foodRepository.countFoods();
        int countMeal = mealRepository.countMeal();
        int countPlan = planRepository.countPlans();
        product.setCustomers(countCustomer);
        product.setFoods(countFoods);
        product.setMeals(countMeal);
        product.setPlan(countPlan);
        return product;
    }
}
