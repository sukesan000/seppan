package com.example.seppan.restController;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.entity.User;
import com.example.seppan.form.EventInfo;
import com.example.seppan.model.DailySummaryModel;
import com.example.seppan.service.CalendarService;
import com.example.seppan.service.MoneyRecordService;
import com.example.seppan.service.UserInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/seppan/top/api")
public class RestTopController {
    @Autowired
    private MoneyRecordService mrService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CalendarService calendarService;

    @GetMapping("/all")
    public String getEvents(@ModelAttribute("eventInfo") EventInfo eventInfo, Model model) throws JsonProcessingException {

        //ログインしているユーザの名前を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authName = auth.getName();

        //ログインユーザの情報取得
        User loginUser = userInfoService.findByName(authName);

        //レコード取得
        List<MoneyRecord> records = mrService.getAllMoneyRecord(loginUser.getUserId());
        //レコードをカレンダー用の変数に収納
        List<DailySummaryModel> events = calendarService.changeRecordToEvent(records);

        String jsonMsg = null;
        try{
            ObjectMapper mapper = new ObjectMapper();
            jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        }catch (JsonProcessingException ioex) {
            System.out.println(ioex.getMessage());
        }
        model.addAttribute("eventInfo", eventInfo);
        return jsonMsg;
    }

    @PostMapping("/editEvent")
    public void editEvent(@RequestBody EventInfo eventInfo) {
        String jsonMsg = null;
        try {
            //ログインしているユーザの名前を取得
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authName = auth.getName();

            //取得した情報をDBに登録する
            mrService.registerMoneyRecord(eventInfo, authName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
