package com.example.plan.plan.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.entity.Customer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.enums.Day;
import com.example.plan.enums.Duration;
import com.example.plan.enums.Gender;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.map.Mapper;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.plan.service.PlanService;
import com.example.plan.utils.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

@Slf4j
@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private PlanRepository planRepository;
    private final Mapper mapper;

    ObjectMapper objectMapper = new ObjectMapper();

    public PlanServiceImpl(Mapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public List<Map<String, Object>> findAll() {
        List<Plan> plans = planRepository.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Plan plan : plans) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", plan.getId());
            map.put("name", plan.getName());
            map.put("startDate", plan.getStartDate());
            map.put("endDate", plan.getEndDate());
            map.put("duration", plan.getDuration());
            mapList.add(map);
        }
        return mapList;
    }

    @Override
    public List<Map<String, Object>> findPlanMealFood() {
        List<Plan> plans = planRepository.findPlansWithMeals();
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (Plan plan : plans) {
            Map<String, Object> planMap = new HashMap<>();
            planMap.put("id", plan.getId());
            planMap.put("name", plan.getName());
            planMap.put("startDate", plan.getStartDate());
            planMap.put("endDate", plan.getEndDate());
            planMap.put("duration", plan.getDuration());

            List<Map<String, Object>> mealList = new ArrayList<>();
            for (Meal meal : plan.getMeals()) {
                Map<String, Object> mealMap = new HashMap<>();
                mealMap.put("mealId", meal.getId());
                mealMap.put("mealName", meal.getName());
                mealMap.put("foods", getFoodsForMeal(meal));
                mealList.add(mealMap);
            }
            planMap.put("meals", mealList);

            mapList.add(planMap);
        }

        return mapList;
    }

    private List<Map<String, Object>> getFoodsForMeal(Meal meal) {
        List<Map<String, Object>> foodList = new ArrayList<>();

        if (meal != null) {
            List<Food> foods = meal.getFoods();

            if (foods != null) {
                for (Food food : foods) {
                    Map<String, Object> foodMap = new HashMap<>();
                    foodMap.put("foodId", food.getId());
                    foodMap.put("foodName", food.getName());
                    foodList.add(foodMap);
                }
            }
        }

        return foodList;
    }

    @Override
    public List<Map<String, Object>> getPlanDetailsByCustomerFullName(String firstName, String lastName) {
        List<Plan> plans = planRepository.findAll();
        List<Map<String, Object>> mapList = new ArrayList<>();

        for (Plan plan : plans) {
            Map<String, Object> planMap = new HashMap<>();
            planMap.put("id", plan.getId());
            planMap.put("name", plan.getName());
            planMap.put("startDate", plan.getStartDate());
            planMap.put("endDate", plan.getEndDate());
            planMap.put("duration", plan.getDuration());

            List<Map<String, Object>> mealList = new ArrayList<>();
            for (Meal meal : plan.getMeals()) {
                Map<String, Object> mealMap = new HashMap<>();
                mealMap.put("mealId", meal.getId());
                mealMap.put("mealName", meal.getName());
                mealMap.put("foods", getFoodsForMeal(meal));
                mealList.add(mealMap);
            }
            planMap.put("meals", mealList);
            mapList.add(planMap);
        }
        return mapList;
    }

    @Override
    public ResponseEntity<ResponseMessage> generateReport(Map<String, Object> requestMap, String firstName, String lastName, LocalDate startDate, LocalDate endDate) {
        try {
            List<Plan> plans = planRepository.findByCustomerName(firstName, lastName, startDate, endDate);
            if (plans == null || !plans.isEmpty()) {
                for (Plan plan : plans) {
                    Map<String, Object> planMap = new HashMap<>();
                    planMap.put("id", plan.getId());
                    planMap.put("name", plan.getName());
                    planMap.put("startDate", plan.getStartDate());
                    planMap.put("endDate", plan.getEndDate());
                    planMap.put("duration", plan.getDuration());
                }

                String filePath = "C:\\Users\\dchatzop\\Downloads\\" + firstName + "_" + lastName + "'" + startDate + "-" + endDate + "'" + "_Report.pdf";

                Document document = new Document(PageSize.A4);
                FileOutputStream fos = new FileOutputStream(filePath);
                PdfWriter.getInstance(document, fos);

                document.open();
                setRectangleInPdf(document);

                String fontPath = "src/main/java/com/example/plan/utils/fonts/Font.ttf";
                String greekText = "Πρόγραμμα Διατροφής";
                BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font headerFont = new Font(bf, 12);
                Font dataFont = new Font(bf,8);
                Font boldFont = new Font(bf,10, Font.BOLD);

                Paragraph title = new Paragraph(greekText, headerFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                for (Plan plan : plans) {
                    String info = "Όνομα:" + firstName + "\n" +
                                    "Επίθετο: " + lastName + "\n" +
                                    "Πλάνο: " + plan.getName() + "\n" +
                                    "Διάρκεια: " + plan.getDuration() + "\n" +
                                    "Ημερομηνίες: " + plan.getStartDate() + " - " + plan.getEndDate() + "\n";
                    Paragraph paragraph = new Paragraph(info, headerFont);
                    document.add(paragraph);
                    document.add(new Paragraph("\n"));
                }
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                String[] columnTitles = {"ΔΕΥΤΕΡΑ", "ΤΡΙΤΗ", "ΤΕΤΑΡΤΗ", "ΠΕΜΠΤΗ", "ΠΑΡΑΣΚΕΥΗ"};

                for (String columnTitle : columnTitles) {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, headerFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(header);
                }

                Map<String, List<Meal>> mealsByDay = new HashMap<>();

                for (Meal meal : plans.get(0).getMeals()) {
                    String day = meal.getDay().toString();
                    mealsByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(meal);
                }

                for (String day : columnTitles) {
                    List<Meal> mealsForDay = mealsByDay.getOrDefault(day, Collections.emptyList());

                    PdfPCell mealCell = new PdfPCell();
                    mealCell.setBorderWidth(1);

                    for (Meal meal : mealsForDay) {
                        String boldText = String.valueOf(meal.getType());
                        String text = "~Όνομα: " + meal.getName() + "\n" +
                                      "~Ποσότητα: " + meal.getQuantity() + "\n" +
                                      "~Γραμμάρια: " + meal.getGram();
                        mealCell.addElement(new Phrase(boldText, boldFont));
                        mealCell.addElement(new Phrase(text, dataFont));
                        mealCell.addElement(new Phrase("\n"));
                    }
                    mealCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    mealCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(mealCell);
                }
                document.add(table);
                document.close();

                String successMessage = "PDF generated successfully";
                ResponseMessage successResponse = new ResponseMessage(successMessage, requestMap);
                return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
            } else {
                String message = "Τα στοιχεία δεν είναι σωστά..";
                ResponseMessage successResponse = new ResponseMessage(message, null);
                return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);
            }


            } catch (Exception ex) {
                ex.printStackTrace();
                String message = "Failed to generate PDF";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    private void setRectangleInPdf(Document document) throws DocumentException {
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addPlan(Map<String, Object> requestMap) {
        try {
            Plan existingPlan = planRepository.findByName((String) requestMap.get("name"));

            if (existingPlan == null) {
                    Plan plan = new Plan();
                    plan.setName((String) requestMap.get("name"));
                    plan.setStartDate(LocalDate.parse(String.valueOf(requestMap.get("startDate"))));
                    plan.setEndDate(LocalDate.parse(String.valueOf(requestMap.get("startDate"))));
                    plan.setDuration(Duration.parse(String.valueOf(requestMap.get("duration"))));
                    planRepository.save(plan);

                    String message = "Το πλάνο γράφτηκε επιτυχώς!";
                    ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);

            } else {
                String message = "Το πλάνο υπάρχει ήδη..";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            } catch(Exception ex){
                log.info("{}", ex);
            }
            String message = PlanConstants.SOMETHING_WENT_WRONG;
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addToPlan(Map<String, List<Object>> requestMap, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);

            if (existingPlan.isPresent()) {
                Plan plan = existingPlan.get();
                addMealsAndFoodsToPlan(plan, requestMap);
            } else {
                List<Object> plans = requestMap.get("plans");
                for (Object objectPlan : plans) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> planData = objectMapper.convertValue(objectPlan, Map.class);
                    String name = String.valueOf(planData.get("name"));
                    LocalDate startDate = LocalDate.parse(String.valueOf(planData.get("startDate")));
                    LocalDate endDate = LocalDate.parse(String.valueOf(planData.get("endDate")));
                    Duration duration = Duration.parse(String.valueOf(planData.get("duration")));

                    Plan plan = new Plan();
                    plan.setName(name);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDuration(duration);
                    addMealsAndFoodsToPlan(plan, requestMap);
                    planRepository.save(plan);
                }
            }

            String message = "Το πλάνο γράφτηκε επιτυχώς!";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addMealsAndFoodsToPlan(Plan plan, Map<String, List<Object>> requestMap) {
        List<Object> meals = requestMap.get("meals");
        if (meals != null) {
            for (Object mealObject : meals) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> mealData = objectMapper.convertValue(mealObject, Map.class);

                String mealName = String.valueOf(mealData.get("name"));
                String mealDescr = String.valueOf(mealData.get("description"));
                Day day = Day.valueOf((String) mealData.get("day"));
                Type mealType = Type.valueOf((String) mealData.get("type"));
                String quantity = String.valueOf(mealData.get("quantity"));
                Meal existingMeal = mealRepository.findByName(mealName);

                if (existingMeal == null) {
                    existingMeal = new Meal();
                    existingMeal.setName(mealName);
                    existingMeal.setDescription(mealDescr);
                    existingMeal.setDay(day);
                    existingMeal.setType(mealType);
                    existingMeal.setQuantity(quantity);
                    mealRepository.save(existingMeal);
                }
                List<Object> foods = (List<Object>) mealData.get("foods");
                if (foods != null) {
                    for (Object foodObject : foods) {
                        Map<String, String> foodData = objectMapper.convertValue(foodObject, Map.class);

                        String foodName = String.valueOf(foodData.get("name"));
                        String foodDescr = String.valueOf(mealData.get("description"));
                        Double gram = Double.valueOf(String.valueOf(foodData.get("gram")));
                        Double calories = Double.valueOf(String.valueOf(foodData.get("calories")));
                        Type foodType = Type.valueOf(foodData.get("type"));

                        Food existingFood = foodRepository.findFoodName(foodName);

                        if (existingFood == null) {
                            existingFood = new Food();
                            existingFood.setName(foodName);
                            existingFood.setDescription(foodDescr);
                            existingFood.setGram(gram);
                            existingFood.setCalories(calories);
                            existingFood.setType(foodType);
                            foodRepository.save(existingFood);
                        }
                        boolean relationshipMealFoodExists = mealRepository.existsMealFoodRelationship(existingMeal, existingFood);

                        if (!relationshipMealFoodExists) {
                            existingMeal.getFoods().add(existingFood);
                        }
                    }
                }

                boolean relationshipPlanMealExists = planRepository.existPlanMealRelationship(plan, existingMeal);

                if (!relationshipPlanMealExists) {
                    plan.getMeals().add(existingMeal);
                }
            }
        }
        planRepository.save(plan);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addMealToPlan(Map<String, List<Object>> requestMap, String nameOfPlan) {
        try {
            Plan existingPlan = planRepository.findByName(nameOfPlan);
            Plan plan;
            if (existingPlan != null) {
                plan = existingPlan;
                addMeals(plan, requestMap);
            } else {
                List<Object> plans = requestMap.get("plans");
                for (Object objectPlan : plans) {

                    Map<String, Object> planData = objectMapper.convertValue(objectPlan, Map.class);
                    String name = String.valueOf(planData.get("name"));
                    LocalDate startDate = LocalDate.parse(String.valueOf(planData.get("startDate")));
                    LocalDate endDate = LocalDate.parse(String.valueOf(planData.get("endDate")));
                    Duration duration = Duration.parse(String.valueOf(planData.get("duration")));

                    plan = new Plan();
                    plan.setName(name);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDuration(duration);
                    addMeals(plan, requestMap);
                    planRepository.save(plan);

                }
            }
            String message = "Το πλάνο γράφτηκε επιτυχώς!";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addMeals(Plan plan, Map<String, List<Object>> requestMap) {
        List<Object> meals = requestMap.get("meals");
        if (meals != null) {
            for (Object mealObject : meals) {

                Map<String, Object> mealData = objectMapper.convertValue(mealObject, Map.class);

                String mealName = String.valueOf(mealData.get("name"));
                String mealDescr = String.valueOf(mealData.get("description"));
                Day day = Day.valueOf((String) mealData.get("day"));
                Double mealGram = Double.valueOf(String.valueOf(mealData.get("gram")));
                String quantity = String.valueOf(mealData.get("quantity"));
                Type mealType = Type.valueOf((String) mealData.get("type"));
                Meal existingMeal = mealRepository.findByName(mealName);

                if (existingMeal == null) {
                    existingMeal = new Meal();
                    existingMeal.setName(mealName);
                    existingMeal.setDescription(mealDescr);
                    existingMeal.setDay(day);
                    existingMeal.setGram(mealGram);
                    existingMeal.setQuantity(quantity);
                    existingMeal.setType(mealType);
                    mealRepository.save(existingMeal);
                }
                List<Object> foods = (List<Object>) mealData.get("foods");
                if (foods != null) {
                    for (Object foodObject : foods) {
                        Map<String, String> foodData = objectMapper.convertValue(foodObject, Map.class);

                        String foodName = String.valueOf(foodData.get("name"));
                        String foodDescr = String.valueOf(mealData.get("description"));
                        Double foodGram = Double.valueOf(String.valueOf(foodData.get("gram")));
                        Double calories = Double.valueOf(String.valueOf(foodData.get("calories")));
                        Type foodType = Type.valueOf(String.valueOf(foodData.get("type")));
                        Food existingFood = foodRepository.findFoodName(foodName);

                        if (existingFood == null) {
                            existingFood = new Food();
                            existingFood.setName(foodName);
                            existingFood.setDescription(foodDescr);
                            existingFood.setGram(foodGram);
                            existingFood.setCalories(calories);
                            existingFood.setType(foodType);
                            foodRepository.save(existingFood);
                        }

                        boolean relationshipMealFoodExists = mealRepository.existsMealFoodRelationship(existingMeal, existingFood);

                        if (!relationshipMealFoodExists) {
                            existingMeal.getFoods().add(existingFood);
                        }
                    }
                }
                planRepository.save(plan);
                mealRepository.save(existingMeal);
                boolean relationshipPlanMealExists = planRepository.existPlanMealRelationship(plan, existingMeal);

                if (!relationshipPlanMealExists) {
                    plan.getMeals().add(existingMeal);
                }
            }
            planRepository.save(plan);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> addCustomerToPlan(Map<String, List<Object>> requestMap, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);
            Plan plan;
            if (existingPlan.isPresent()) {
                plan = existingPlan.get();
                addCustomer(plan, requestMap);
            } else {
                List<Object> plans = requestMap.get("plans");
                for (Object objectPlan : plans) {

                    Map<String, Object> planData = objectMapper.convertValue(objectPlan, Map.class);
                    String name = String.valueOf(planData.get("name"));
                    LocalDate startDate = LocalDate.parse(String.valueOf(planData.get("startDate")));
                    LocalDate endDate = LocalDate.parse(String.valueOf(planData.get("endDate")));
                    Duration duration = Duration.parse(String.valueOf(planData.get("duration")));
                    plan = new Plan();
                    plan.setName(name);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDuration(duration);
                    addCustomer(plan, requestMap);
                    planRepository.save(plan);
                }
            }

            String message = "Το πλάνο γράφτηκε επιτυχώς!";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = PlanConstants.SOMETHING_WENT_WRONG;
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addCustomer(Plan plan, Map<String, List<Object>> requestMap) {
        try {
            List<Object> customers = requestMap.get("customers");
            if (customers != null) {
                for (Object customerObject : customers) {

                    Map<String, Object> customerData = objectMapper.convertValue(customerObject, Map.class);

                    String customerFirstname = String.valueOf(customerData.get("firstName"));
                    String lastName = String.valueOf(customerData.get("lastName"));
                    String email = String.valueOf(customerData.get("email"));
                    String phone = String.valueOf(customerData.get("phone"));
                    String city = String.valueOf(customerData.get("city"));
                    String address = String.valueOf(customerData.get("address"));
                    LocalDate birthday = LocalDate.parse(String.valueOf(customerData.get("birthday")));
                    Gender gender = Gender.valueOf((String) customerData.get("gender"));
                    List<Customer> existingCustomer = customerRepository.findCustomerByName(customerFirstname, lastName);

                    if (existingCustomer != null) {
                        Customer customer = new Customer();
                        customer.setFirstName(customerFirstname);
                        customer.setLastName(lastName);
                        customer.setEmail(email);
                        customer.setPhone(phone);
                        customer.setCity(city);
                        customer.setAddress(address);
                        customer.setBirthday(birthday);
                        customer.setGender(gender);
                        customerRepository.save(customer);
                        plan.getCustomers().add(customer);
                    }
                }
            }
            planRepository.save(plan);
        } catch (DataIntegrityViolationException ex) {
            log.error("Could not add Customer. Unique constraint violation..{}", ex);
        }
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> updatePlan(Map<String, String> requestMap, int id) {
        try {
            Optional<Plan> optionalPlan = planRepository.findById(id);
            if (optionalPlan.isPresent()) {
                Plan updatePlan = optionalPlan.get();
                updatePlan.setName(requestMap.get("name"));
                updatePlan.setDuration(Duration.parse(requestMap.get("duration")));
                updatePlan.setStartDate(LocalDate.parse(requestMap.get("startDate")));
                updatePlan.setEndDate(LocalDate.parse(requestMap.get("endDate")));
                planRepository.save(updatePlan);
                String message = "Το πλάνο " + "'" + requestMap.get("name") + " ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> deletePlan(int id) {
        try {
            Plan optionalPlan = planRepository.findPlanById(id);
            if (!optionalPlan.equals(null)) {
                planRepository.delete(optionalPlan);
                String message = "Το πλάνο " + "'" + optionalPlan.getName() + " διαγράφηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Transactional
    @Override
    public ResponseEntity<ResponseMessage> removeCustomerFromPlan(int customerId, int PlanId) {
        try {
            Optional<Customer> existingCustomer = customerRepository.findById(customerId);
            Optional<Plan> existingPlan = planRepository.findById(PlanId);

            if (existingCustomer.isPresent() && existingPlan.isPresent()) {
                existingPlan.get().getCustomers().remove(existingCustomer.get());
                planRepository.save(existingPlan.get());
                customerRepository.deleteById(customerId);

                String message = "Ο πελάτης " + "'" + existingCustomer.get().getFirstName() + " " + existingCustomer.get().getLastName() + "'" + " αφαιρέθηκε επιτυχώς από το πλάνο " + "'" + existingPlan.get().getName() + "'";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                String message = "Το πλάνο ΔΕΝ βρέθηκε..";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}