package com.example.plan.plan.service.Impl;

import com.example.plan.constants.PlanConstants;
import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.customer.repository.CustomerRepository;
import com.example.plan.dto.plan.WholePlanDto;
import com.example.plan.enums.Day;
import com.example.plan.enums.Duration;
import com.example.plan.enums.Gender;
import com.example.plan.enums.Type;
import com.example.plan.food.entity.Food;
import com.example.plan.food.repository.FoodRepository;
import com.example.plan.food.service.FoodService;
import com.example.plan.map.Mapper;
import com.example.plan.meal.entity.Meal;
import com.example.plan.meal.repository.MealRepository;
import com.example.plan.plan.entity.Plan;
import com.example.plan.plan.repository.PlanRepository;
import com.example.plan.plan.service.PlanService;
import com.example.plan.utils.ResponseMessage;
import com.example.plan.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MealRepository mealRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private FoodService foodService;
    @Autowired
    private PlanRepository planRepository;
    private final Mapper mapper;

    @Value("${file.dest-dir}")
    private String destDir;

    ObjectMapper objectMapper = new ObjectMapper();

    public PlanServiceImpl(Mapper mapper) {
        this.mapper = mapper;
    }


    @Override
    public ResponseEntity<ResponseMessage> showWholePlan(Map<String, Object> request, String firstname, String surname) {
        List<Map<String, Object>> findWholePlan = planRepository.displayWholePlan(firstname, surname);

            WholePlanDto wholePlanDto = new WholePlanDto();
        if (!findWholePlan.isEmpty()) {
            for (Map<String, Object> wholePlan : findWholePlan) {
                wholePlanDto.setName(wholePlan.get("name").toString());
                wholePlanDto.setStartDate((LocalDate) wholePlan.get("startDate"));
                wholePlanDto.setEndDate((LocalDate) wholePlan.get("endDate"));
                wholePlanDto.setMealName((String) wholePlan.get("mealName"));
                wholePlanDto.setDay((Day) wholePlan.get("day"));
                wholePlanDto.setType((Type) wholePlan.get("type"));
                wholePlanDto.setCalories((Double) wholePlan.get("calories"));
                wholePlanDto.setCustomerFullName((String) wholePlan.get("customerFullName"));
                wholePlanDto.setMealStartDate((LocalDate) wholePlan.get("mealStartDate"));
                wholePlanDto.setMealEndDate((LocalDate) wholePlan.get("mealEndDate"));
            }
            String message = "Το πλάνο " + " βρέθηκε επιτυχώς!";
            ResponseMessage response = new ResponseMessage(message, findWholePlan);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public List<Map<String, Object>> findAll() {
        return planRepository.findAll().stream()
                .map(plan -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", plan.getId());
                    map.put("name", plan.getName());
                    map.put("startDate", plan.getStartDate());
                    map.put("endDate", plan.getEndDate());
                    map.put("duration", plan.getDuration());
                    return map;
                }).collect(Collectors.toList());
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
    public List<Map<String, Object>> getPlanDetailsByCustomerFullName(String firstname, String surname, LocalDate birthdate) {
        /*--------------review--------------*/
        List<Plan> plans = planRepository.findPlanByCustomerName(firstname, surname, birthdate);
        List<Map<String, Object>> mapList = new ArrayList<>();



        for (Plan plan : plans) {
            Map<String, Object> planMap = new HashMap<>();
            planMap.put("id", plan.getId());
            planMap.put("name", plan.getName());
            planMap.put("startDate", plan.getStartDate());
            planMap.put("endDate", plan.getEndDate());
            planMap.put("duration", plan.getDuration());

            List<Map<String, Object>> customerList = plan.getCustomers().stream()
                    .filter(customer -> Objects.equals(customer.getFirstname(), firstname) &&
                                        Objects.equals(customer.getSurname(), surname) &&
                                        Objects.equals(customer.getBirthday(), birthdate))
                    .map(customer -> {
                        Map<String, Object> customerMap = new HashMap<>();
                        customerMap.put("firstname", customer.getFirstname());
                        customerMap.put("surname", customer.getSurname());
                        /*customerMap.put("email", customer.getEmail());
                        customerMap.put("phone", customer.getPhone());*/
                        customerMap.put("city", customer.getCity());
                        customerMap.put("address", customer.getAddress());
                        customerMap.put("birthday", customer.getBirthday());
                        customerMap.put("gender", customer.getGender());
                        return customerMap;
                    }).collect(Collectors.toList());


            List<Map<String, Object>> mealList = plan.getMeals().stream()
                    .map(meal -> {
                        Map<String, Object> mealMap = new HashMap<>();
                        mealMap.put("mealId", meal.getId());
                        mealMap.put("mealName", meal.getName());
                        mealMap.put("foods", getFoodsForMeal(meal));
                        return mealMap;
            }).collect(Collectors.toList());

            planMap.put("customers", customerList);
            planMap.put("meals", mealList);
            mapList.add(planMap);
        }
        return mapList;
    }

    @Override
    public ResponseEntity<ResponseMessage> count() {
        int countCustomers = customerRepository.countCustomer();
        int countPlans = planRepository.countPlans();
        int countMeals = mealRepository.countMeal();
        int countFoods = foodRepository.countFoods();
        String message = "Πελάτες: " + "'" + countCustomers + "'" + "   " +
                            "Πλάνα φαγητού: " + "'" + countPlans + "'" + "   " +
                            "Φαγητά: " + "'" + countFoods + "'" + "   " +
                            "Γεύματα: " +  "'" + countMeals + "'";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> generateReport(Map<String, Object> requestMap, String firstname, String surname, LocalDate startDate, LocalDate endDate) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate startDateFormat = LocalDate.parse(startDate.format(dateFormatter), dateFormatter);
            LocalDate endDateFormat = LocalDate.parse(endDate.format(dateFormatter), dateFormatter);
            List<Plan> plans = planRepository.findByCustomerNameAndDates(firstname, surname, startDate, endDate);
            if (plans == null || !plans.isEmpty()) {
                for (Plan plan : plans) {
                    Map<String, Object> planMap = new HashMap<>();
                    planMap.put("id", plan.getId());
                    planMap.put("name", plan.getName());
                    planMap.put("startDate", plan.getStartDate().format(dateFormatter));
                    planMap.put("endDate", plan.getEndDate().format(dateFormatter));
                    planMap.put("duration", plan.getDuration());
                }

                String filePath = destDir + firstname + "_" + surname + "_" + startDate + "__" + endDate + "_Report.pdf";

                Document document = new Document(PageSize.A4);
                FileOutputStream fos = new FileOutputStream(filePath);
                PdfWriter.getInstance(document, fos);

                document.open();
                setRectangleInPdf(document);

                String fontPath = "src/main/java/com/example/plan/utils/fonts/Font.ttf";
                String greekText = "Πρόγραμμα Διατροφής";
                BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font headerFont = new Font(bf, 18);
                Font informationFont = new Font(bf, 14);
                Font columnFont = new Font(bf, 12);
                Font dataFont = new Font(bf,8);
                Font boldFont = new Font(bf,10, Font.BOLD);

                Paragraph title = new Paragraph(greekText, headerFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                for (Plan plan : plans) {
                    String info = "Όνομα: " + firstname + "\n" +
                                    "Επίθετο: " + surname + "\n" +
                                    "Πλάνο: " + plan.getName() + "\n" +
                                    "Διάρκεια: " + plan.getDuration() + "\n" +
                                    "Ημερομηνίες: " + plan.getStartDate().format(dateFormatter) + " - " + plan.getEndDate().format(dateFormatter) + "\n";
                    Paragraph paragraph = new Paragraph(info, informationFont);
                    document.add(paragraph);

                    /*String imagePath = "src/main/java/com/example/plan/utils/images/image.jpg";
                    Image image = Image.getInstance(imagePath);
                    image.setAlignment(Element.ALIGN_CENTER);
                    document.add(image);
                    document.add(new Paragraph("\n\n"));*/
                }
                //document.newPage();
                document.add(new Paragraph("\n\n"));
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                String[] columnTitles = {"ΔΕΥΤΕΡΑ", "ΤΡΙΤΗ", "ΤΕΤΑΡΤΗ", "ΠΕΜΠΤΗ"};

                for (String columnTitle : columnTitles) {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, columnFont));
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

                    double totalCalories = 0;

                    for (Meal meal : mealsForDay) {
                        String boldText = String.valueOf(meal.getType());

                        StringBuilder textBuilder = new StringBuilder();
                        if (meal.getCalories() != null) {
                            totalCalories += meal.getCalories();
                        }
                        textBuilder.append("~Γεύμα: ").append(meal.getName()).append("\n")
                            .append("~Λεπτομέρεις: ").append(meal.getDescription()).append("\n")
                            .append("~Θερμήδες: ").append(meal.getCalories()).append("\n");

                        mealCell.addElement(new Phrase(boldText, boldFont));
                        mealCell.addElement(new Phrase(textBuilder.toString(), dataFont));
                        mealCell.addElement(new Phrase("\n"));
                    }
                    DecimalFormat formatter = new DecimalFormat("#.0");
                    String formatTotalCalories = formatter.format(totalCalories);
                    String totalCaloriesText = "Συνολικές Θερμίδες: " + formatTotalCalories;
                    mealCell.addElement(new Phrase(totalCaloriesText,boldFont));
                    mealCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    mealCell.setVerticalAlignment(Element.ALIGN_TOP);
                    mealCell.setPaddingBottom(0);
                    table.addCell(mealCell);
                }
                document.add(table);
                document.newPage();

                PdfPTable table2 = new PdfPTable(3);
                String[] columnTitles2 = {"ΠΑΡΑΣΚΕΥΗ", "ΣΑΒΒΑΤΟ", "ΚΥΡΙΑΚΗ"};
                for (String columnTitle : columnTitles2) {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, columnFont));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table2.addCell(header);
                }

                Map<String, List<Meal>> mealsByDay2 = new HashMap<>();

                for (Meal meal : plans.get(0).getMeals()) {
                    String day = meal.getDay().toString();
                    mealsByDay2.computeIfAbsent(day, k -> new ArrayList<>()).add(meal);
                }

                for (String day : columnTitles2) {
                    List<Meal> mealsForDay = mealsByDay2.getOrDefault(day, Collections.emptyList());

                    PdfPCell mealCell = new PdfPCell();
                    mealCell.setBorderWidth(1);

                    double totalCalories = 0;

                    for (Meal meal : mealsForDay) {
                        String boldText = String.valueOf(meal.getType());
                        StringBuilder textBuilder = new StringBuilder();
                        if (meal.getCalories() != null) {
                            totalCalories += meal.getCalories();
                        }
                        textBuilder.append("~Γεύμα: ").append(meal.getName()).append("\n")
                                .append("~Λεπτομέρεις: ").append(meal.getDescription()).append("\n")
                                .append("~Θερμήδες: ").append(meal.getCalories()).append("\n");


                        mealCell.addElement(new Phrase(boldText, boldFont));
                        mealCell.addElement(new Phrase(textBuilder.toString(), dataFont));
                        mealCell.addElement(new Phrase("\n"));
                    }
                    DecimalFormat formatter = new DecimalFormat("#.0");
                    String formatTotalCalories = formatter.format(totalCalories);
                    String totalCaloriesText = "Συνολικές Θερμίδες: " + formatTotalCalories;
                    mealCell.addElement(new Phrase(totalCaloriesText,boldFont));
                    mealCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    mealCell.setVerticalAlignment(Element.ALIGN_TOP);
                    table2.addCell(mealCell);
                }

                document.add(table2);
                document.close();

                String successMessage = "Το PDF δημιουργήθηκε επιτυχώς";
                ResponseMessage successResponse = new ResponseMessage(successMessage, requestMap);
                return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
            }
            String message = "Τα στοιχεία δεν είναι σωστά..";
            ResponseMessage successResponse = new ResponseMessage(message, null);
            return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);

            } catch (Exception ex) {
                ex.printStackTrace();
            String message = "Αποτυχία δημιυργίας PDF..";
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

    @Override
    public ResponseEntity<ResponseMessage> addPlan(Map<String, Object> requestMap) {
        try {
            Plan existingPlan = planRepository.findByName((String) requestMap.get("name"));

            if (existingPlan == null) {
                    Plan plan = new Plan();
                    plan.setName((String) requestMap.get("name"));
                    plan.setStartDate(LocalDate.parse(String.valueOf(requestMap.get("startDate"))));
                    plan.setEndDate(LocalDate.parse(String.valueOf(requestMap.get("endDate"))));
                    plan.setDuration(Duration.parse(String.valueOf(requestMap.get("duration"))));
                    planRepository.save(plan);

                    String message = "Το πλάνο γράφτηκε επιτυχώς!";
                    ResponseMessage response = new ResponseMessage(message, requestMap);
                    return new ResponseEntity<>(response, HttpStatus.CREATED);
            }
            String message = "Το πλάνο υπάρχει ήδη..";
            ResponseMessage response = new ResponseMessage(message, requestMap);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

            } catch(Exception ex){
                log.info("{}", ex);
            }
            String message = PlanConstants.SOMETHING_WENT_WRONG;
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    @Override
    public ResponseEntity<ResponseMessage> addToPlan(Map<String, List<Object>> requestMap, int id) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(id);

            if (existingPlan.isPresent()) {
                Plan plan = existingPlan.get();
                addMealsFoodsCustomerToPlan(plan, requestMap);
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
                    addMealsFoodsCustomerToPlan(plan, requestMap);
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

    private void addMealsFoodsCustomerToPlan(Plan plan, Map<String, List<Object>> requestMap) {
        List<Object> meals = requestMap.get("meals");
            for (Object mealObject : meals) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> mealData = objectMapper.convertValue(mealObject, Map.class);

                String mealName = String.valueOf(mealData.get("name"));
                String mealDescr = String.valueOf(mealData.get("description"));
                Day day = Day.valueOf((String) mealData.get("day"));
                Type mealType = Type.valueOf((String) mealData.get("type"));
                Double calories = ((Double) mealData.get("calories"));
                Meal existingMeal = mealRepository.findByNameDayType(mealName, day, mealType);

                if (existingMeal == null) {
                    existingMeal = new Meal();
                    existingMeal.setName(mealName);
                    existingMeal.setDescription(mealDescr);
                    existingMeal.setDay(day);
                    existingMeal.setType(mealType);
                    existingMeal.setCalories(calories);
                    mealRepository.save(existingMeal);
                }
                List<Object> foods = (List<Object>) mealData.get("foods");
                if (foods != null) {
                    for (Object foodObject : foods) {
                        Map<String, String> foodData = objectMapper.convertValue(foodObject, Map.class);

                        String foodName = String.valueOf(foodData.get("name"));
                        String foodDescr = String.valueOf(mealData.get("description"));
                        String foodQuantiity = String.valueOf(foodData.get("quantity"));
                        //Double calories = Double.valueOf(String.valueOf(foodData.get("calories")));
                        Type foodType = Type.valueOf(foodData.get("type"));

                        Food existingFood = foodRepository.findFoodName(foodName);

                        if (existingFood == null) {
                            existingFood = new Food();
                            existingFood.setName(foodName);
                            existingFood.setDescription(foodDescr);
                            existingFood.setQuantity(foodQuantiity);
                            existingFood.setCalories(calories);
                            existingFood.setType(foodType);
                            foodRepository.save(existingFood);
                        }
                    }
                }
                        List<Object> customer = requestMap.get("customer");
                        Map<String, Object> customerData = objectMapper.convertValue(customer.get(0), Map.class);

                        String firstname = String.valueOf(customerData.get("firstname"));
                        String surname = String.valueOf(customerData.get("surname"));
                        String city = String.valueOf(customerData.get("city"));
                        String address = String.valueOf(customerData.get("address"));
                        LocalDate birthday = LocalDate.parse(String.valueOf(customerData.get("birthday")));
                        Gender gender = Gender.valueOf((String) customerData.get("gender"));
                        DietCustomer existingCustomer = customerRepository.findCustomerByName(firstname, surname, birthday);


                        if (existingCustomer == null) {
                            DietCustomer newCustomer = new DietCustomer();
                            newCustomer.setFirstname(firstname);
                            newCustomer.setSurname(surname);
                            newCustomer.setCity(city);
                            newCustomer.setAddress(address);
                            newCustomer.setBirthday(birthday);
                            newCustomer.setGender(gender);
                            customerRepository.save(newCustomer);
                            plan.getCustomers().add(newCustomer);
                        }

                        /*boolean relationshipMealFoodExists = mealRepository.existsMealFoodRelationship(existingMeal, existingFood);
                        boolean relationshipPlanCustomerExists = planRepository.existsCustomerPlanRelationship(plan, existingCustomer);
                        if (!relationshipPlanCustomerExists) {
                            plan.getCustomers().add(existingCustomer);
                        }
                        if (!relationshipMealFoodExists) {
                            existingMeal.getFoods().add(existingFood);
                        }*/
            }


                /*boolean relationshipPlanMealExists = planRepository.existPlanMealRelationship(plan, existingMeal);

                if (!relationshipPlanMealExists) {
                    plan.getMeals().add(existingMeal);
                }*/

        planRepository.save(plan);
    }

    @Override
    public ResponseEntity<ResponseMessage> addMealToPlan(Map<String, List<Object>> requestMap, String nameOfPlan, LocalDate startDate, LocalDate endDate) {
        try {
            Plan existingPlan = planRepository.findByNameAndDates(nameOfPlan, startDate, endDate);
            Plan plan;
            if (existingPlan != null) {
                plan = existingPlan;
                addMeals(plan, requestMap);
                addCustomer(plan, requestMap);
            } else {
                List<Object> plans = requestMap.get("plans");
                for (Object objectPlan : plans) {

                    Map<String, Object> planData = objectMapper.convertValue(objectPlan, Map.class);
                    String name = String.valueOf(planData.get("name"));
                     startDate = LocalDate.parse(String.valueOf(planData.get("startDate")));
                     endDate = LocalDate.parse(String.valueOf(planData.get("endDate")));
                    Duration duration = Duration.parse(String.valueOf(planData.get("duration")));

                    plan = new Plan();
                    plan.setName(name);
                    plan.setStartDate(startDate);
                    plan.setEndDate(endDate);
                    plan.setDuration(duration);
                    addMeals(plan, requestMap);
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

    private void addMeals(Plan plan, Map<String, List<Object>> requestMap) {
        List<Object> meals = requestMap.get("meals");
        if (meals != null) {
            for (Object mealObject : meals) {

                Map<String, Object> mealData = objectMapper.convertValue(mealObject, Map.class);
                if (mealData.size() <= 1) {
                    continue;
                }
                String mealName = String.valueOf(mealData.get("name"));
                String mealDescr = String.valueOf(mealData.get("description"));
                Day day = Day.valueOf((String) mealData.get("day"));
                Type mealType = Type.valueOf((String) mealData.get("type"));
                Double mealCalories = (Double) mealData.get("calories");
                LocalDate mealStartDate = LocalDate.parse((String) mealData.get("startDate"));
                LocalDate mealEndDate = LocalDate.parse((String) mealData.get("endDate"));

                Meal meal = mealRepository.findByNameDayType(mealName, day, mealType);
                //boolean existingMeal = meal.getName().equals(mealName) && meal.getDay().equals(day) && meal.getType().equals(mealType);
                if (meal == null) {
                    Meal newMeal = new Meal();
                    newMeal.setName(mealName);
                    newMeal.setDescription(mealDescr);
                    newMeal.setDay(day);
                    newMeal.setType(mealType);
                    newMeal.setCalories(mealCalories);
                    newMeal.setMealStartDate(mealStartDate);
                    newMeal.setMealEndDate(mealEndDate);
                    mealRepository.save(newMeal);
                    plan.getMeals().add(newMeal);
                }
               /* List<Object> foods = (List<Object>) mealData.get("foods");
                if (foods != null) {
                    for (Object foodObject : foods) {
                        Map<String, String> foodData = objectMapper.convertValue(foodObject, Map.class);

                        String foodName = String.valueOf(foodData.get("name"));
                        String foodDescr = String.valueOf(foodData.get("description"));
                        String foodQuantiity = String.valueOf(foodData.get("quantity"));
                        Double foodCalories = Double.valueOf(String.valueOf(foodData.get("calories")));
                        Type foodType = Type.valueOf(String.valueOf(foodData.get("type")));
                        Food existingFood = foodRepository.findFoodName(foodName);

                        if (existingFood == null) {
                            existingFood = new Food();
                            existingFood.setName(foodName);
                            existingFood.setDescription(foodDescr);
                            existingFood.setQuantity(foodQuantiity);
                            existingFood.setCalories(foodCalories);
                            existingFood.setType(foodType);
                            foodRepository.save(existingFood);
                        }

                        boolean relationshipMealFoodExists = mealRepository.existsMealFoodRelationship(meal, existingFood);

                        if (meal != null && !relationshipMealFoodExists) {
                            meal.getFoods().add(existingFood);
                        }
                    }
                }*/
                if (plan != null) {
                    planRepository.save(plan);
                }
                if (meal != null) {
                    mealRepository.save(meal);
                }
                boolean relationshipPlanMealExists = planRepository.existPlanMealRelationship(plan, meal);

                if (!relationshipPlanMealExists) {
                    plan.getMeals().add(meal);
                }
            }
            planRepository.save(plan);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> addCustomerToPlan(Map<String, List<Object>> requestMap, String nameOfPlan) {
        try {
            Plan existingPlan = planRepository.findByName(nameOfPlan);
            Plan plan;
            if (existingPlan != null) {
                plan = existingPlan;
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
                    planRepository.save(plan);
                    addCustomer(plan, requestMap);

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

                    String customerfirstname = String.valueOf(customerData.get("firstname"));
                    String surname = String.valueOf(customerData.get("surname"));
                    String city = String.valueOf(customerData.get("city"));
                    String address = String.valueOf(customerData.get("address"));
                    LocalDate birthday = LocalDate.parse(String.valueOf(customerData.get("birthday")));
                    Gender gender = Gender.valueOf((String) customerData.get("gender"));
                    DietCustomer existingCustomer = customerRepository.findCustomerByName(customerfirstname, surname, birthday);


                    if (existingCustomer == null) {
                        DietCustomer newCustomer = new DietCustomer();
                        newCustomer.setFirstname(customerfirstname);
                        newCustomer.setSurname(surname);
                        newCustomer.setCity(city);
                        newCustomer.setAddress(address);
                        newCustomer.setBirthday(birthday);
                        newCustomer.setGender(gender);
                        customerRepository.save(newCustomer);
                        plan.getCustomers().add(newCustomer);
                    }
                    boolean relationshipPlanCustomerExists = planRepository.existsCustomerPlanRelationship(plan, existingCustomer);
                    if (!relationshipPlanCustomerExists) {
                        plan.getCustomers().add(existingCustomer);
                    }
                }
            }
            planRepository.save(plan);
        } catch (DataIntegrityViolationException ex) {
            log.error("Could not add Customer. Unique constraint violation..{}", ex);
        }
    }

    @Override
    public ResponseEntity<ResponseMessage> updatePlan(Map<String, Object> requestMap, int id) {
        try {
            Optional<Plan> optionalPlan = planRepository.findById(id);
            if (optionalPlan.isPresent()) {
                Plan updatePlan = optionalPlan.get();
                updatePlan.setName((String) requestMap.get("name"));
                updatePlan.setStartDate(Utils.formatter(requestMap));
                updatePlan.setEndDate(Utils.secondFormatter(requestMap));
                updatePlan.setDuration(Duration.parse((String) requestMap.get("duration")));
                planRepository.save(updatePlan);

                String message = "Το πλάνο " + "'" + requestMap.get("name") + " ενημερώθηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, requestMap);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Το πλάνο ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> deletePlan(int id) {
        try {
            Plan optionalPlan = planRepository.findPlanById(id);
            if (!optionalPlan.equals(null)) {
                planRepository.delete(optionalPlan);
                String message = "Το πλάνο " + "'" + optionalPlan.getName() + " διαγράφηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "Το πλάνο ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> removeCustomerFromPlan(Long customerId, int PlanId) {
        try {
            Optional<DietCustomer> existingCustomer = customerRepository.findById(customerId);
            Optional<Plan> existingPlan = planRepository.findById(PlanId);

            if (existingCustomer.isPresent() && existingPlan.isPresent()) {
                existingPlan.get().getCustomers().remove(existingCustomer.get());
                planRepository.save(existingPlan.get());
                customerRepository.deleteById(customerId);

                String message = "Ο πελάτης " + "'" + existingCustomer.get().getFirstname() + " " + existingCustomer.get().getSurname() + "'" + " αφαιρέθηκε επιτυχώς από το πλάνο " + "'" + existingPlan.get().getName() + "'";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            String message = "ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> removeFoodFromMeal(int planId, int mealId, int foodId) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(planId);
            Optional<Meal> existingMeal =  mealRepository.findById(mealId);
            Optional<Food> existingFood =  foodRepository.findById(foodId);
            if (existingPlan.isPresent()) {
                existingPlan.get().getMeals().remove(existingMeal.get());
                planRepository.save(existingPlan.get());
            }

            if (existingMeal.isPresent() && existingFood.isPresent()) {
                existingMeal.get().getFoods().remove(existingFood.get());
                mealRepository.save(existingMeal.get());
                //foodRepository.deleteById(foodId);

                String message = "Το φαγητό " + "'" + existingFood.get().getName() + "'" + " αφαιρέθηκε επιτυχώς από το γεύμα" + "'" + existingMeal.get().getName() + "'";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            }
            String message = "Το γεύμα ή το φαγητό ΔΕΝ βρέθηκαν..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<ResponseMessage> deleteMealAndFood(int planId, int mealId, int foodId) {
        try {
            Optional<Plan> existingPlan = planRepository.findById(planId);
            Optional<Meal> existingMeal =  mealRepository.findById(mealId);
            Optional<Food> existingFood =  foodRepository.findById(foodId);
            if (existingPlan.isPresent()) {
                existingPlan.get().getMeals().remove(existingMeal.get());
                planRepository.save(existingPlan.get());
            }

            if (existingMeal.isPresent() && existingFood.isPresent() ){
                existingMeal.get().getFoods().remove(existingFood.get());
                mealRepository.save(existingMeal.get());
                mealRepository.deleteById(mealId);
                foodRepository.deleteById(foodId);
                String message = "Το γεύμα " + "'" + existingMeal.get().getName() + "'" + " και το φαγητό " + "'" + existingFood.get().getName()+ "'" + " διαγράφηκαν επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response , HttpStatus.OK);
            }
            String message = "Το γεύμα ΔΕΝ βρέθηκε..";
            ResponseMessage response = new ResponseMessage(message, null);
            return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteAll() {
        try {
                planRepository.deleteAll();
                mealRepository.deleteAll();
                foodRepository.deleteAll();
               // customerRepository.deleteAll();
                String message = "Το πλάνο " + " διαγράφηκε επιτυχώς!";
                ResponseMessage response = new ResponseMessage(message, null);
                return new ResponseEntity<>(response, HttpStatus.OK);


        } catch (Exception ex) {
            log.info("{}", ex);
        }
        String message = "Κάτι πήγε λάθος..";
        ResponseMessage response = new ResponseMessage(message, null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}