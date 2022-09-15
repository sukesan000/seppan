package com.example.seppan.service;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.form.DatePeriod;
import com.example.seppan.form.EventInfo;

import java.util.List;

public interface MoneyRecordService {
    void registerMoneyRecord(EventInfo info, String userName);
    void updateMoneyRecord(EventInfo info, String userName);
    List<MoneyRecord> getAllMoneyRecord(int userId, int partnerId);
    void deleteOne(int recordId);
    int calcMoneyRecord(String authName, DatePeriod datePeriod);
}
