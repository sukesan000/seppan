package com.example.seppan.restController;

import com.example.seppan.model.DailySummaryModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/event")
public class RestTopController {
    @GetMapping("/all")
    public String getEvents() throws JsonProcessingException {
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
        return jsonMsg;
    }
}
