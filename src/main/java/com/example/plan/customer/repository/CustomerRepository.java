package com.example.plan.customer.repository;

import com.example.plan.customer.entity.DietCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CustomerRepository extends JpaRepository<DietCustomer, Long> {


    @Query("SELECT c FROM DietCustomer c  WHERE c.firstname=:firstname and c.surname=:surname and c.birthday=:birthday")
    DietCustomer findCustomerByName(String firstname, String surname, LocalDate birthday);

    @Query("SELECT c FROM DietCustomer c WHERE c.email=:email")
    DietCustomer findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(c) FROM DietCustomer c")
    int countCustomer();

    @Query("SELECT c FROM DietCustomer c  WHERE c.id=:id")
    DietCustomer findCustomerById(@Param("id") Long id);

}
