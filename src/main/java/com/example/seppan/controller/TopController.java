package com.example.seppan.controller;

import com.example.seppan.entity.User;
import com.example.seppan.form.EventInfo;
import com.example.seppan.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/seppan")
public class TopController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/top")
    public String top(Model model){
        List<String> shared_users = new ArrayList<>();
        //ログインしているユーザの名前を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authName = auth.getName();

        //ログインユーザの情報取得
        User loginUser = userInfoService.findByName(authName);
        model.addAttribute("loginUser", loginUser);
         shared_users.add(loginUser.getUserName());

        //ログインユーザと共有関係にあるユーザがいれば情報取得
        int sharedUserNo = loginUser.getSharedUser();
        if(sharedUserNo != 0){
            User shared_user = userInfoService.findById(sharedUserNo);
            shared_users.add(shared_user.getUserName());
            model.addAttribute("users", shared_users);
        }
        return "top";
    }
}
