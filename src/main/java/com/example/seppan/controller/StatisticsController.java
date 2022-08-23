package com.example.seppan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatisticsController {
    @GetMapping("/statistics")
    public String statistics(Model model){
        return "statistics";
    }
}
