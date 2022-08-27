package com.example.seppan.service;

import com.example.seppan.dao.CategoryDao;
import com.example.seppan.dao.UserInfoDao;
import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.entity.User;
import com.example.seppan.model.DailySummaryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarServiceImpl implements CalendarService{

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    UserInfoDao userInfoDao;

    //レコードをカレンダー用の変数に入れ直す
    @Override
    public List<DailySummaryModel> changeRecordToEvent(List<MoneyRecord> records) {
        List<DailySummaryModel> summaryModelList  = new ArrayList<>();
        for(MoneyRecord record : records){
            DailySummaryModel summary = new DailySummaryModel();
            User user = userInfoDao.findByUserId(record.getPayerId());
            //カテゴリ名取得
            String categoryName = categoryDao.findNameById(record.getCategoryId());
            //カテゴリ名と金額を結合
            String title = categoryName + ": " + String.valueOf(record.getPrice() + "円");
            summary.setTitle(title);
            summary.setStart(record.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            summary.setCategoryId(record.getCategoryId());
            summary.setPrice(record.getPrice());
            summary.setOwnPayment(record.getOwnPayment());
            summary.setPartnerPayment(record.getPartnerPayment());
            summary.setRecordId(record.getId());
            summary.setPayerId(record.getPayerId());
            summary.setRemarks(record.getRecordNote());
            summaryModelList.add(summary);
        }
        return summaryModelList;
    }
}
