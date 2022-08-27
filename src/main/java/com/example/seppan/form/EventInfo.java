package com.example.seppan.form;

import lombok.Data;


@Data
public class EventInfo {
    private String recordId;
    private String money;
    private String ownPayment;
    private String partnerPayment;
    private String categoryId;
    private String date;
    private String payerId;
    private String remarks;
}
