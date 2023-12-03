package com.example.plan.customer.repository;

import com.example.plan.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    @Query("SELECT c FROM Customer c  WHERE c.firstName=:firstName and c.lastName=:lastName")
    List<Customer> findCustomerByName(String firstName, String lastName);

    @Query("SELECT c FROM Customer c WHERE c.email=:email")
    Customer findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(c) FROM Customer c")
    int countCustomer();

}
