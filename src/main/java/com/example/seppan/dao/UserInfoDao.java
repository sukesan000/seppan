package com.example.seppan.dao;

import com.example.seppan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao extends JpaRepository<User, Integer> {
    User findByUserName(String name);
    User findByUserId(int sharedUserNo);
}
