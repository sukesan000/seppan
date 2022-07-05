package com.example.seppan.dao;

import com.example.seppan.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class LoginUserDao {
    @Autowired
    EntityManager em;

    public LoginUser findUser(String userName){
        String query = "";
        query += "SELECT * ";
        query += "FROM users ";
        query += "WHERE name = :userName "; //setParameterで引数の値を代入できるようにNamedParameterを利用

        //EntityManagerで取得された結果はオブジェクトとなるので、LoginUser型へキャストが必要となる
        return (LoginUser)em.createNativeQuery(query, LoginUser.class).setParameter("userName", userName)
                .getSingleResult();
    }

}
