package com.example.seppan.service;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.form.EventInfo;

public interface MoneyRecordService {
    EventInfo registerMoneyRecord(EventInfo info, String UserName);
}
