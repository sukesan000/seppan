package com.example.seppan.dao;

import com.example.seppan.entity.MoneyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyRecordDao extends JpaRepository<MoneyRecord, Integer> {
}
