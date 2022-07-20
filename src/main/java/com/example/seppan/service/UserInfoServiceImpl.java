package com.example.seppan.service;

import com.example.seppan.dao.UserInfoDao;
import com.example.seppan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService{

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public User findByName(String name) {
        User user = userInfoDao.findByUserName(name);
        return user;
    }

    @Override
    public User findById(int sharedUserNo) {
        User user = userInfoDao.findByUserId(sharedUserNo);
        return user;
    }
}
