package com.example.seppan.controller;

import com.example.seppan.form.EventInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seppan")
public class TopController {

    @GetMapping("/top")
    public String top(Model model, EventInfo eventInfo){
        model.addAttribute("eventInfo", new EventInfo());
        return "top";
    }
}
