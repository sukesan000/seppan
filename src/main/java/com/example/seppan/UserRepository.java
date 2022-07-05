package com.example.seppan;

import com.example.seppan.entity.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<LoginUser, Integer> {
    @Query(value = "select * from users where name = ?1", nativeQuery = true)
    LoginUser findUser(String name);
}
