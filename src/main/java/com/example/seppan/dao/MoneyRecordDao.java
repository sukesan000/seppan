package com.example.seppan.dao;

import com.example.seppan.entity.MoneyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoneyRecordDao extends JpaRepository<MoneyRecord, Integer> {
    List<MoneyRecord> findByUserIdIs(int userId);
}
