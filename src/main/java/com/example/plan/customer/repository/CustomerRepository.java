package com.example.plan.customer.repository;

import com.example.plan.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query("SELECT c FROM Customer c  WHERE c.firstName=:firstName and c.lastName=:lastName and c.birthday=:birthday")
    Customer findCustomerByName(String firstName, String lastName, LocalDate birthday);

    @Query("SELECT c FROM Customer c WHERE c.email=:email")
    Customer findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(c) FROM Customer c")
    int countCustomer();

    @Query("SELECT c FROM Customer c  WHERE c.id=:id")
    Customer findCustomerById(@Param("id") Long id);

}
