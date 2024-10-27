package com.example.plan.customerInfo.repository;

import com.example.plan.customer.entity.DietCustomer;
import com.example.plan.customerInfo.entity.CustomerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerInfoRepository extends JpaRepository<CustomerInfo, Long> {

    @Query("SELECT c FROM DietCustomer c  WHERE c.id=:id")
    DietCustomer findCustomerById(@Param("id") Long id);

    @Query("SELECT ci FROM CustomerInfo ci JOIN ci.customer cu WHERE cu.id=:id")
    Optional<DietCustomer> findCustomerInfoByCustomer(@Param("id") Long id);

    List<CustomerInfo> findByCustomer(DietCustomer customer);

    @Query("SELECT ci FROM CustomerInfo ci JOIN ci.customer cu WHERE cu.id=:id")
    List<CustomerInfo> findCustomerInfosByCustomer(@Param("id") Long id);

    @Query("SELECT ci FROM CustomerInfo ci WHERE ci.customer.id=:id ORDER BY ci.createdDate DESC")
    List<CustomerInfo> findLatestCustomerInfos(@Param("id") Long id);

    @Query("SELECT ci FROM CustomerInfo ci " +
            "WHERE ci.createdDate = (SELECT MAX(ci2.createdDate) FROM CustomerInfo ci2 WHERE ci2.customer.id = :id)")
    List<CustomerInfo> findLatestCustomerInfoByCustomerId(@Param("id") Long id);

    @Query("SELECT ci FROM CustomerInfo ci WHERE ci.createdDate=:createdDate")
    CustomerInfo findLatestCustomerInfo(LocalDateTime createdDate);
}
