package com.example.seppan.form;

import lombok.Data;


@Data
public class EventInfo {
    private int money;
    private String category;
    private String date;
    private String payer;
    private String remarks;
}
