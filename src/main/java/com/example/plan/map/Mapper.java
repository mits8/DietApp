package com.example.plan.map;

import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.dto.plan.PlanDTO;
import com.example.plan.dto.customer.CustomerDTO;
import com.example.plan.dto.customer.CustomerPlanDTO;
import com.example.plan.dto.food.FoodDTO;
import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.dto.meal.MealFoodPlanDTO;
import com.example.plan.dto.plan.PlanMealCustomerDTO;
import com.example.plan.food.entity.Food;
import com.example.plan.meal.entity.Meal;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class Mapper {

    @Autowired
    private PlanRepository planRepository;

    private DietCustomer customer = new DietCustomer();
    private CustomerDTO customerDTO = new CustomerDTO();
    private CustomerPlanDTO customerPlanDTO = new CustomerPlanDTO();

    private Food food = new Food();
    private FoodDTO foodDTO = new FoodDTO();
    private List<Food> foods = new ArrayList<>();

    private Meal meal = new Meal();
    private MealDTO mealDTO = new MealDTO();
    private MealFoodDTO mealFoodDTO = new MealFoodDTO();

    private Plan plan = new Plan();
    private PlanDTO planDTO = new PlanDTO();
    private MealFoodPlanDTO mealFoodPlanDTO = new MealFoodPlanDTO();
    private PlanMealCustomerDTO PlanMealCustomerDTO = new PlanMealCustomerDTO();

    public PlanMealCustomerDTO mapPlanToMealDTO(Plan plan) {
        PlanMealCustomerDTO.setId(plan.getId());
        PlanMealCustomerDTO.setName(plan.getName());
        PlanMealCustomerDTO.setStartDate(plan.getStartDate());
        PlanMealCustomerDTO.setEndDate(plan.getEndDate());

        if (!Objects.isNull(plan.getMeals())) {
            List<MealDTO> mealDTOs = plan.getMeals().stream()
                    .map(this::mapMealToMealDTO)
                    .collect(Collectors.toList());
            PlanMealCustomerDTO.setMeals(mealDTOs);
        }
        List<CustomerDTO> customerDTOs = plan.getCustomers().stream()
                .map(this::mapCustomerToCustomerDTO)
                .collect(Collectors.toList());
        PlanMealCustomerDTO.setCustomers(customerDTOs);
        return PlanMealCustomerDTO;

    }

    public MealFoodDTO mapMealToMealFoodDTO(Meal meal) {
        mealFoodDTO.setName(meal.getName());
        mealFoodDTO.setDescription(meal.getDescription());
        mealFoodDTO.setType(meal.getType());
        if(!Objects.isNull(meal.getFoods())) {
            List<FoodDTO> foodDTOS = meal.getFoods().stream()
                    .map(this::mapFoodToFoodDTO)
                    .collect(Collectors.toList());
            mealFoodDTO.setFoodDTOS(foodDTOS);
        }
        return mealFoodDTO;
    }

    public MealFoodPlanDTO mapMealToMealFoodPlanDTO(Meal meal) {
        mealFoodPlanDTO.setName(meal.getName());
        mealFoodPlanDTO.setDescription(meal.getDescription());
        mealFoodPlanDTO.setType(meal.getType());
        mealFoodPlanDTO.setDay(meal.getDay());
        return mealFoodPlanDTO;
    }

    public CustomerPlanDTO customerPlanDTO(DietCustomer customer) {
        customerPlanDTO.setId(customer.getId());
        customerPlanDTO.setFirstname(customer.getFirstname());
        customerPlanDTO.setSurname(customer.getSurname());
        /*customerPlanDTO.setEmail(customer.getEmail());
        customerPlanDTO.setPhone(customer.getPhone());*/

        List<PlanDTO> PlanDTOS = customer.getPlans().stream()
                .map(this::PlanToPlanDTO)
                .collect(Collectors.toList());
        customerPlanDTO.setPlanDTOS(PlanDTOS);
        return customerPlanDTO;
    }

    public MealDTO mapMealToMealDTO(Meal meal) {
        mealDTO.setId(meal.getId());
        mealDTO.setName(meal.getName());
        mealDTO.setDescription(meal.getDescription());
        mealDTO.setDay(meal.getDay());
        mealDTO.setType(meal.getType());
        return mealDTO;
    }

    public Meal mapMealDTOToMeal(MealDTO mealDTO) {
        mapMealCommonProperties(meal, mealDTO);
        return meal;
    }

    public Meal mapMealFoodDTOToMeal(MealFoodDTO mealFoodDTO) {
        meal.setId(mealDTO.getId());
        meal.setName(mealDTO.getName());
        meal.setDescription(mealDTO.getDescription());
        meal.setDay(mealDTO.getDay());
        meal.setType(mealDTO.getType());
        if(mealFoodDTO.getFoodDTOS() != null) {
            List<Food> foods = mealFoodDTO.getFoodDTOS().stream()
                    .map(this::mapFoodDTOToFood)
                    .collect(Collectors.toList());
            meal.setFoods(foods);
        }
        return meal;
    }


    public Food mapMealFoodDTOToFood(MealFoodDTO mealFoodDTO){
        food.setName(mealFoodDTO.getName());
        food.setDescription(mealFoodDTO.getDescription());
        food.setCalories(mealFoodDTO.getCalories());
        food.setType(mealFoodDTO.getType());
        return food;
    }

    public CustomerDTO mapCustomerToCustomerDTO(DietCustomer customer){
        customerDTO.setId(customer.getId());
        customerDTO.setFirstname(customer.getFirstname());
        customerDTO.setSurname(customer.getSurname());
        /*customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());*/
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setBirthday(customer.getBirthday());
        customerDTO.setGender(customer.getGender());
        return customerDTO;
    }

    public DietCustomer mapCustomerDTOToCustomer(CustomerDTO customerDTO) {
        DietCustomer customer = new DietCustomer();
        mapCustomerCommonProperties(customer, customerDTO);
        return customer;
    }

    public DietCustomer mapCustomerPlanDTOToCustomer(CustomerPlanDTO customerPlanDTO) {
        DietCustomer customer = new DietCustomer();
        mapCustomerCommonProperties(customer, customerPlanDTO);
        return customer;
    }


    public PlanDTO PlanToPlanDTO(Plan plan) {
        planDTO.setId(plan.getId());
        planDTO.setName(plan.getName());
        planDTO.setStartDate(plan.getStartDate());
        planDTO.setEndDate(plan.getEndDate());
        if (!Objects.isNull(plan.getMeals()) && (!Objects.isNull(plan.getCustomers()))) {
            List<MealDTO> mealDTOs = plan.getMeals().stream()
                    .map(this::mapMealToMealDTO)
                    .collect(Collectors.toList());
            planDTO.setMealDTOS(mealDTOs);

            List<CustomerDTO> customerDTOS = plan.getCustomers().stream()
                    .map(this::mapCustomerToCustomerDTO)
                    .collect(Collectors.toList());
            planDTO.setCustomerDTOS(customerDTOS);

        }


        return planDTO;
    }

    public Plan mapPlanDTOToPlan(PlanDTO planDTO, int id) {
        Optional<Plan> existingPlan = planRepository.findById(id);
        Plan plan = existingPlan.get();
        plan.setId(existingPlan.get().getId());
        plan.setName(existingPlan.get().getName());
        plan.setDuration(existingPlan.get().getDuration());
        plan.setStartDate(existingPlan.get().getStartDate());
        plan.setEndDate(planDTO.getEndDate());
        if (planDTO.getMealDTOS() != null) {
            List<Meal> meals= planDTO.getMealDTOS().stream()
                    .map(this::mapMealDTOToMeal)
                    .collect(Collectors.toList());
            plan.setMeals(meals);
        }
        return plan;
    }
    public Plan mapPlanDTOToPlan(PlanDTO planDTO) {
        plan.setId(planDTO.getId());
        plan.setName(planDTO.getName());
        plan.setDuration(planDTO.getDuration());
        plan.setStartDate(planDTO.getStartDate());
        plan.setEndDate(planDTO.getEndDate());
        if (planDTO.getMealDTOS() != null) {
            List<Meal> meals= planDTO.getMealDTOS().stream()
                    .map(this::mapMealDTOToMeal)
                    .collect(Collectors.toList());
            plan.setMeals(meals);
        }
        return plan;
    }

    public FoodDTO mapFoodToFoodDTO(Food food) {
        foodDTO.setName(food.getName());
        foodDTO.setDescription(food.getDescription());

        foodDTO.setCalories(food.getCalories());
        foodDTO.setType(food.getType());
        return foodDTO;
    }

    public Food mapFoodDTOToFood(FoodDTO foodDTO) {
        mapFoodCommonProperties(food, foodDTO);
        return food;
    }

    public void mapCustomerCommonProperties(DietCustomer customer, CustomerDTO customerDTO) {
        customer.setId(customerDTO.getId());
        customer.setFirstname(customerDTO.getFirstname());
        customer.setSurname(customerDTO.getSurname());
        /*customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());*/
        customer.setCity(customerDTO.getCity());
        customer.setAddress(customerDTO.getAddress());
        customer.setBirthday(customerDTO.getBirthday());
        customer.setGender(customerDTO.getGender());
    }

    public void mapFoodCommonProperties(Food food, FoodDTO foodDTO) {
        food.setName(foodDTO.getName());
        food.setDescription(foodDTO.getDescription());

        food.setCalories(foodDTO.getCalories());
        food.setType(foodDTO.getType());
    }

    public void mapMealCommonProperties(Meal meal, MealDTO mealDTO) {
        meal.setId(mealDTO.getId());
        meal.setName(mealDTO.getName());
        meal.setDescription(mealDTO.getDescription());
        meal.setDay(mealDTO.getDay());
        meal.setType(mealDTO.getType());
    }


    public Mapper() {
    }
}
