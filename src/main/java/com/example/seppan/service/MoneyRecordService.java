package com.example.seppan.service;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.form.EventInfo;

import java.util.List;

public interface MoneyRecordService {
    void registerMoneyRecord(EventInfo info, String userName);
    void updateMoneyRecord(EventInfo info, String userName);
    List<MoneyRecord> getAllMoneyRecord(int userId);
    void deleteOne(int recordId);
}
