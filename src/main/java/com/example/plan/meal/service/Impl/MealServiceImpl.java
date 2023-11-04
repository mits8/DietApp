package com.example.plan.meal.service.Impl;

import com.example.plan.dto.meal.MealDTO;
import com.example.plan.dto.meal.MealFoodDTO;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.map.Mapper;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.meal.service.MealService;
import com.example.plan.utils.meal.MealResponseMessage;
import com.example.plan.plan.repository.PlanRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MealServiceImpl implements MealService {
    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PlanRepository PlanRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public List<MealDTO> findAll() {
        List<Meal> meals = mealRepository.findAll();
        List<MealDTO> mealDTOS = meals.stream()
                .map(mapper::mapMealToMealDTO)
                .collect(Collectors.toList());
        return mealDTOS;
    }

    @Override
    public MealDTO findById(int id) {
        Optional<Meal> existingMeal = mealRepository.findById(id);
        Meal meal = null;
        if (existingMeal.isPresent()) {
            meal = existingMeal.get();
            MealDTO mealDTO = mapper.mapMealToMealDTO(meal);
            return mealDTO;
        } else {
            throw new RuntimeException("Το γεύμα " + "'" + existingMeal.get().getName() + "'" + " δεν βρέθηκε..");
        }
    }



    @Override
    public List<MealDTO> findByTypeBreakfast(Type type) {
        //type = Type.BREAKFAST;
        List<Meal> meals = mealRepository.findByType(type);
        List<MealDTO> mealDTOS = meals.stream()
                .map(mapper::mapMealToMealDTO)
                .collect(Collectors.toList());
        return mealDTOS;
    }
    @Transactional
    @Override
    public ResponseEntity<MealResponseMessage> addMeal(MealDTO mealDTO) {
        try {
            Meal name = mealRepository.findByName(mealDTO.getName());
            if (Objects.isNull(name)) {
                Meal meal = mapper.mapMealDTOToMeal(mealDTO);
                mealRepository.save(meal);
                String message = "Το γεύμα " + "'" + mealDTO.getName() + "'" + " γράφτηκαν επιτυχώς!";
                MealResponseMessage response = new MealResponseMessage(message, mealDTO);
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }else {
                String message = "Το γεύμα " + "'" + mealDTO.getName() + "'" + " υπάρχει ήδη..";
                MealResponseMessage response = new MealResponseMessage(message, mealDTO);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<MealResponseMessage> addMealWithFoods(MealFoodDTO mealFoodDTO) {
        Meal name = mealRepository.findByName(mealFoodDTO.getName());
        if (Objects.isNull(name)) {
            Meal meal = mapper.mapMealFoodDTOToMeal(mealFoodDTO);
            List<Food> foods = mealFoodDTO.getFoodDTOS().stream()
                    .filter(foodDTO -> foodDTO.getId() == 0)
                    .map(foodDTO -> {
                        Food food = mapper.mapFoodDTOToFood(foodDTO);
                        return foodRepository.save(food);
                    }).collect(Collectors.toList());
            meal.setFoods(foods);
            mealRepository.save(meal);
            String message = "Το γεύμα " + "'" + mealFoodDTO.getName() + " και τα φαγητά γράφτηκαν επιτυχώς!";
            MealResponseMessage response = new MealResponseMessage(message, mealFoodDTO);
            return new ResponseEntity<>(response , HttpStatus.OK);
        }
        String message = "Κάτι πήγε λάθος..";
        MealResponseMessage response = new MealResponseMessage(message, (MealFoodDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<MealResponseMessage> addFoodToMeal(MealFoodDTO mealFoodDTO, int id) {
        try {
            Optional<Meal> existingMeal =  mealRepository.findById(id);
            if (existingMeal.isPresent()){
                Meal updateMeal = existingMeal.get();
                List<Food> foods = mealFoodDTO.getFoodDTOS().stream()
                        .map(foodDTO -> {
                            Food food = mapper.mapFoodDTOToFood(foodDTO);
                            return foodRepository.save(food);
                        }).toList();
                updateMeal.getFoods().addAll(foods);
                mealRepository.save(updateMeal);
                mealFoodDTO = mapper.mapMealToMealFoodDTO(updateMeal);
                String message = "Το γεύμα " + "'" + existingMeal.get().getName() + " ενημερώθηκε επιτυχώς!";
                MealResponseMessage response = new MealResponseMessage(message, mealFoodDTO);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το γεύμα ΔΕΝ βρέθηκε..";
                MealResponseMessage response = new MealResponseMessage(message, mealFoodDTO);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        MealResponseMessage response = new MealResponseMessage(message, (MealFoodDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<MealResponseMessage> updateMeal(MealDTO mealDTO, int id) {
        try {
            Optional<Meal> existingMeal =  mealRepository.findById(id);
            if (existingMeal.isPresent()){
                Meal updateMeal = existingMeal.get();
                updateMeal.setName(mealDTO.getName());
                updateMeal.setDescription(mealDTO.getDescription());
                updateMeal.setQuantity(mealDTO.getQuantity());
                updateMeal.setDay(mealDTO.getDay());
                updateMeal.setType(mealDTO.getType());
                mealRepository.save(updateMeal);
                String message = "Το γεύμα " + "'" + mealDTO.getName() + " ενημερώθηκε επιτυχώς!";
                MealResponseMessage response = new MealResponseMessage(message, mealDTO);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το γεύμα ΔΕΝ βρέθηκε..";
                MealResponseMessage response = new MealResponseMessage(message, mealDTO);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @Transactional
    @Override
    public ResponseEntity<MealResponseMessage> deleteMeal(int id) {
        try {
            Optional<Meal> optionalMeal =  mealRepository.findById(id);
            if (optionalMeal.isPresent()){
                mealRepository.delete(optionalMeal.get());
                String message = "Το γεύμα " + "'" + optionalMeal.get().getName() + " διαγράφηκε επιτυχώς!";
                MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το γεύμα ΔΕΝ βρέθηκε..";
                MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<MealResponseMessage> deleteMealAndFood(int mealId, int foodId) {
        try {
            Optional<Meal> existingMeal =  mealRepository.findById(mealId);
            Optional<Food> existingFood =  foodRepository.findById(foodId);

            if (existingMeal.isPresent() && existingFood.isPresent() ){
                mealRepository.deleteById(mealId);
                foodRepository.deleteById(foodId);

                String message = "Το γεύμα " + "'" + existingMeal.get().getName() + "'" + " και το φαγητό " + "'" + existingFood.get().getName()+ "'" + " διαγράφηκαν επιτυχώς!";
                MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το γεύμα ΔΕΝ βρέθηκε..";
                MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
                return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<MealResponseMessage> removeFoodFromMeal(int mealId, int foodId) {
        try {
            Optional<Meal> existingMeal = mealRepository.findById(mealId);
            Optional<Food> existingFood = foodRepository.findById(foodId);

            if (existingMeal.isPresent() && existingFood.isPresent()) {
                existingMeal.get().getFoods().remove(existingFood.get());
                mealRepository.save(existingMeal.get());
                foodRepository.deleteById(foodId);

                String message = "Το φαγητό " + "'" + existingFood.get().getName() + "'" + " αφαιρέθηκε επιτυχώς από το γεύμα" + "'" + existingMeal.get().getName() + "'";
                MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            } else {
                String message = "Το γεύμα ή το φαγητό ΔΕΝ βρέθηκαν..";
                MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        MealResponseMessage response = new MealResponseMessage(message, (MealDTO) null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
