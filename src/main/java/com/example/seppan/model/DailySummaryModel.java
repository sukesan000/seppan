package com.example.seppan.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DailySummaryModel implements Serializable {
    private String title;
    private String start;
    private String end;
}
