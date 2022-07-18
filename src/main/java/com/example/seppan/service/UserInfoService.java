package com.example.seppan.service;

import com.example.seppan.entity.User;

public interface UserInfoService {
    User findByName(String name);
}
