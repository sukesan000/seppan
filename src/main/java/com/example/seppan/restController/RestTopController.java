package com.example.seppan.restController;

import com.example.seppan.form.EventInfo;
import com.example.seppan.model.DailySummaryModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestTopController {
    @GetMapping("/all")
    public String getEvents(@ModelAttribute("eventInfo") EventInfo eventInfo, Model model) throws JsonProcessingException {
        String jsonMsg = null;
        try {
            List<DailySummaryModel> events = new ArrayList<>();
            DailySummaryModel event = new DailySummaryModel();
            event.setTitle("first event");
            event.setStart("2022-05-14");
            event.setEnd("2022-05-15");
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
        model.addAttribute("eventInfo", eventInfo);
        return jsonMsg;
    }

    @PostMapping("/editEvent")
    public void editEvent(@ModelAttribute EventInfo eventInfo) {
        if(eventInfo.getCategory().isEmpty()) {
            System.out.println("成功");
        }else{
            System.out.println("失敗");
        }
    }

}
