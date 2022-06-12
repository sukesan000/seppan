package com.example.seppan.form;

import lombok.Data;

import java.util.Date;

@Data
public class EventInfo {
    private int money;
    private String category;
    private String date;
    private String remarks;
}
