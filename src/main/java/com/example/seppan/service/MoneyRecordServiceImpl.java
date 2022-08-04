package com.example.seppan.service;

import com.example.seppan.dao.MoneyRecordDao;
import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.entity.User;
import com.example.seppan.form.EventInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class MoneyRecordServiceImpl implements MoneyRecordService{

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    MoneyRecordDao moneyRecordDao;

    @Override
    public EventInfo registerMoneyRecord(EventInfo info, String userName) {
        MoneyRecord record = new MoneyRecord();
        try {
            LocalDate date = LocalDate.parse(info.getDate());

            //現在時刻取得
            LocalDateTime nowDate = LocalDateTime.now();

            //登録者のid取得
            User user = userInfoService.findByName(userName);

            record.setPrice(Integer.parseInt(info.getMoney()));
            record.setCategoryId(Integer.parseInt(info.getCategoryId()));
            record.setRecordNote(info.getRemarks());
            record.setDate(date);
            record.setCreatedAt(nowDate);
            record.setUpdatedAt(null);
            record.setUserId(user.getUserId());
            record.setPayerId(Integer.parseInt(info.getPayerId()));
            moneyRecordDao.save(record);

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<MoneyRecord> getAllMoneyRecord(int userId) {
        List<MoneyRecord> records = moneyRecordDao.findByUserIdIs(userId);
        return records;
    }
}
