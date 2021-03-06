package com.example.seppan.restController;

import com.example.seppan.form.EventInfo;
import com.example.seppan.model.DailySummaryModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/seppan/top/api")
public class RestTopController {
    @GetMapping("/all")
    public String getEvents(@ModelAttribute("eventInfo") EventInfo eventInfo, Model model) throws JsonProcessingException {
        String jsonMsg = null;
        try {
            List<DailySummaryModel> events = new ArrayList<>();
            DailySummaryModel event = new DailySummaryModel();
            event.setTitle("first event");
            event.setStart("2022-06-14");
            events.add(event);

            event = new DailySummaryModel();
            event.setTitle("second event");
            event.setStart("2022-06-24");
            events.add(event);

            //FullCalendar pass encoded string
            ObjectMapper mapper = new ObjectMapper();
            jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (JsonProcessingException ioex) {
            System.out.println(ioex.getMessage());
        }
        model.addAttribute("eventInfo", eventInfo);
        return jsonMsg;
    }

    @PostMapping("/editEvent")
    public void editEvent(@RequestBody EventInfo eventInfo) {
        String jsonMsg = null;
        try {
            //取得した情報をDBに登録する


            //表示させる情報をeventsにまとめ出力


            List<DailySummaryModel> events = new ArrayList<>();
            DailySummaryModel event = new DailySummaryModel();
            event.setTitle("first event");
            event.setStart(eventInfo.getDate());
            events.add(event);

            event = new DailySummaryModel();
            event.setTitle("second event");
            event.setStart("2022-05-24");
            event.setEnd("2022-05-25");
            events.add(event);

            //FullCalendar pass encoded string
            ObjectMapper mapper = new ObjectMapper();
            jsonMsg =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(events);
        } catch (JsonProcessingException ioex) {
            System.out.println(ioex.getMessage());
        }
    }

}
