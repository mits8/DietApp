package com.example.plan.user.repository;

import com.example.plan.user.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    @Query("select u from UserInfo u where u.contactInfo.email = :email")
    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findUserInfoById(int id);

    @Query("select u from UserInfo u join u.contactInfo c where c.email= :email")
    UserInfo findUserByEmail(@Param("email") String email);

}