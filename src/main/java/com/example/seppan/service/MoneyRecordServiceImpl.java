package com.example.seppan.service;

import com.example.seppan.dao.MoneyRecordDao;
import com.example.seppan.entity.MoneyRecord;
import com.example.seppan.entity.User;
import com.example.seppan.form.DatePeriod;
import com.example.seppan.form.EventInfo;
import com.example.seppan.restController.RestTopController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoneyRecordServiceImpl implements MoneyRecordService{

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    MoneyRecordDao moneyRecordDao;

    Logger logger = LoggerFactory.getLogger(RestTopController.class);

    //レコード追加
    @Override
    public void registerMoneyRecord(EventInfo info, String userName) {
        MoneyRecord record = new MoneyRecord();
        try {
            LocalDate date = LocalDate.parse(info.getDate());

            //現在時刻取得
            LocalDateTime nowDate = LocalDateTime.now();

            //登録者のid取得
            User user = userInfoService.findByName(userName);

            record.setPrice(Integer.parseInt(info.getMoney()));
            record.setOwnPayment(Integer.parseInt(info.getOwnPayment()));
            record.setPartnerPayment(Integer.parseInt(info.getPartnerPayment()));
            record.setCategoryId(Integer.parseInt(info.getCategoryId()));
            record.setRecordNote(info.getRemarks());
            record.setDate(date);
            record.setCreatedAt(nowDate);
            record.setUpdatedAt(nowDate);
            record.setUserId(user.getUserId());
            record.setPayerId(Integer.parseInt(info.getPayerId()));
            moneyRecordDao.save(record);

            logger.info("レコード登録 recordId: userId:{} price:{}", user.getUserId(), info.getMoney());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //レコード更新
    @Override
    public void updateMoneyRecord(EventInfo info, String userName) {
        try {
            LocalDate date = LocalDate.parse(info.getDate());

            //現在時刻取得
            LocalDateTime nowDate = LocalDateTime.now();

            //登録者のid取得
            User user = userInfoService.findByName(userName);

            //該当の問合せ取得
            MoneyRecord record = moneyRecordDao.findById(Integer.parseInt(info.getRecordId()));

            record.setPrice(Integer.parseInt(info.getMoney()));
            record.setOwnPayment(Integer.parseInt(info.getOwnPayment()));
            record.setPartnerPayment(Integer.parseInt(info.getPartnerPayment()));
            record.setCategoryId(Integer.parseInt(info.getCategoryId()));
            record.setRecordNote(info.getRemarks());
            record.setUpdatedAt(nowDate);
            record.setUserId(user.getUserId());
            record.setPayerId(Integer.parseInt(info.getPayerId()));
            moneyRecordDao.save(record);

            logger.info("レコード更新 recordId:{} userId:{} price:{}", record.getId(),user.getUserId(), info.getMoney());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //レコード全件取得
    @Override
    public List<MoneyRecord> getAllMoneyRecord(int userId, int partnerId) {
        List<MoneyRecord> records = moneyRecordDao.findByUserIdAndSharedUserId(userId, partnerId);
        return records;
    }

    //レコード一件削除
    @Override
    public void deleteOne(int recordId) {
        moneyRecordDao.deleteById(recordId);
        logger.info("レコード削除 recordId:{} userId: price:", recordId);
    }

    @Override
    public int calcMoneyRecord(String authName, DatePeriod datePeriod) {

        User user = userInfoService.findByName(authName);
        int ownId = user.getUserId();
        int partnerId = user.getSharedUser();
        //自分が支払った時の共有部分
        int ownTotalPrice = 0;
        //相手が支払った時の共有部分
        int partnerTotalPrice = 0;
        //自分が支払った時の相手の品物の合計
        int totalPartnerPayment = 0;
        //相手が支払った時の自分の品物の合計
        int totalOwnPayment = 0;
        //精算金額
        int adjustmentAmount;

        List<MoneyRecord> ownRecordList = moneyRecordDao.findRecordListByIdFromTo(ownId,datePeriod.getDateFrom(),datePeriod.getDateTo());
        List<MoneyRecord> partnerRecordList = moneyRecordDao.findRecordListByIdFromTo(partnerId,datePeriod.getDateFrom(),datePeriod.getDateTo());

        for(MoneyRecord record: ownRecordList){
            //共有部分を求める
            int price = record.getPrice();
            int ownPrice = record.getOwnPayment();
            int partnerPrice = record.getPartnerPayment();
            totalPartnerPayment += partnerPrice;
            ownTotalPrice += price - (ownPrice + partnerPrice);
        }

        for(MoneyRecord record: partnerRecordList){
            //共有部分を求める
            int price = record.getPrice();
            int ownPrice = record.getOwnPayment();
            int partnerPrice = record.getPartnerPayment();
            totalOwnPayment += ownPrice;
            partnerTotalPrice += price - (ownPrice + partnerPrice);
        }

        adjustmentAmount = ((ownTotalPrice / 2) + totalPartnerPayment) - ((partnerTotalPrice / 2) + totalOwnPayment);

//        (A + C) - (B + D)
//
//        A 自分が支払った共有部分/ 2
//        B 相手が支払った共有部分/ 2
//        C 自分が支払った時の相手の品物の合計 partner_payment
//        D 相手が支払った時の自分の品物の合計 own_payment
        return adjustmentAmount;
    }

}
