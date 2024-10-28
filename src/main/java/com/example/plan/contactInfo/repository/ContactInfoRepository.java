package com.example.plan.contactInfo.repository;

import com.example.plan.contactInfo.entity.ContactInfo;
import com.example.plan.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {


}
