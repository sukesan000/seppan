package com.example.seppan.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DailySummaryModel implements Serializable {
    private String title;
    private String start;
    private String end;
    private int CategoryId;
    private int price;
    private int ownPayment;
    private int partnerPayment;
    private int recordId;
    private int payerId;
    private String remarks;
}
