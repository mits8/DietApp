package com.example.plan.customer.repository;

import com.example.plan.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    @Query("SELECT c FROM Customer c  WHERE c.firstName=:firstName and c.lastName=:lastName")
    Customer findCustomerByName(String firstName, String lastName);

    @Query("SELECT c FROM Customer c WHERE c.lastName=:lastName")
    Customer findByName (String lastName);

    Customer findByEmail(String email);


}
