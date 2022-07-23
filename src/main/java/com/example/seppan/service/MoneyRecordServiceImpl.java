package com.example.seppan.service;

import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.form.EventInfo;
import org.springframework.stereotype.Service;

@Service
public class MoneyRecordServiceImpl implements MoneyRecordService{
    @Override
    public EventInfo registerMoneyRecord(EventInfo info) {
        MoneyRecord record = new MoneyRecord();

        record.setPrice(info.getMoney());
        record.setCategoryId(info.getCategoryId());
        record.setPrice(info.getMoney());
        record.setPrice(info.getMoney());
        record.setPrice(info.getMoney());
        record.setPrice(info.getMoney());

        return null;
    }
}
