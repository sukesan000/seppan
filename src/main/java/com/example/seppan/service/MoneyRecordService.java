package com.example.seppan.service;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.form.EventInfo;

import java.util.List;

public interface MoneyRecordService {
    EventInfo registerMoneyRecord(EventInfo info, String UserName);
    List<MoneyRecord> getAllMoneyRecord(int userId);
    void deleteOne(int recordId);
}
