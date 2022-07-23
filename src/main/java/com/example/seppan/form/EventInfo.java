package com.example.seppan.form;

import lombok.Data;


@Data
public class EventInfo {
    private int money;
    private int categoryId;
    private String date;
    private String payer;
    private String remarks;
}
