package com.example.seppan.restController;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.entity.User;
import com.example.seppan.form.DatePeriod;
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
    @Autowired
    private MoneyRecordService moneyRecordService;

    //レコード全取得
    @GetMapping("/all")
    public String getEvents(@ModelAttribute("eventInfo") EventInfo eventInfo, Model model) throws JsonProcessingException {

        //ログインしているユーザの名前を取得
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authName = auth.getName();

        //ログインユーザの情報取得
        User loginUser = userInfoService.findByName(authName);
        //共有ユーザのid取得
        int sharedUserId = loginUser.getSharedUser();

        //レコード取得
        List<MoneyRecord> records = mrService.getAllMoneyRecord(loginUser.getUserId(), loginUser.getSharedUser());
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

    //レコード追加
    @PostMapping("/addEvent")
    public void addEvent(@RequestBody EventInfo eventInfo) {
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

    //レコード削除
    @PostMapping("/deleteEvent")
    public void deleteEvent(@RequestBody int recordId){
        try{
            moneyRecordService.deleteOne(recordId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //レコード更新
    @PostMapping("/updateEvent")
    public void updateEvent(@RequestBody EventInfo eventInfo){
        try {
            //ログインしているユーザの名前を取得
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authName = auth.getName();

            //取得した情報をDBで更新する
            mrService.updateMoneyRecord(eventInfo, authName);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //精算
    @GetMapping("/adjustment")
    public int adjustment(@RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo, Model model){
        int adjustmentAmount = 0;
        try {
            //ログインしているユーザの名前を取得
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String authName = auth.getName();

            DatePeriod datePeriod = new DatePeriod();
            datePeriod.setDateFrom(dateFrom);
            datePeriod.setDateTo(dateTo);

            //特定の期間の精算を行う
            adjustmentAmount = mrService.calcMoneyRecord(authName, datePeriod);
            model.addAttribute("adjustmentAmount", adjustmentAmount);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return adjustmentAmount;
    }
}
