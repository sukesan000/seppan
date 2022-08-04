package com.example.seppan.service;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.form.EventInfo;
import com.example.seppan.model.DailySummaryModel;

import java.util.List;

public interface CalendarService {
    List<DailySummaryModel> changeRecordToEvent(List<MoneyRecord> records);
}
