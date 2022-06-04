package com.example.seppan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seppan")
public class TopController {

    @GetMapping("/top")
    public String top(){
        return "top";
    }
}
