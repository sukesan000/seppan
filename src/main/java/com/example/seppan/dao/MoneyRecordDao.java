package com.example.seppan.dao;

import com.example.seppan.entity.MoneyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface MoneyRecordDao extends JpaRepository<MoneyRecord, Integer> {
    List<MoneyRecord> findByUserIdIs(int userId);

    @Query(value = "select * from money_records where payer_iD = :userId OR payer_iD = :partnerId", nativeQuery = true)
    List<MoneyRecord> findByUserIdAndSharedUserId(int userId,int partnerId);

    @Query(value = "select * from money_records where payer_id = :payerId and date between :dateFrom and :dateTo", nativeQuery = true)
    List<MoneyRecord> findRecordListByIdFromTo(int payerId, String dateFrom, String dateTo);

    MoneyRecord findById(int recordId);
}
