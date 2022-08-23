package com.example.seppan.entity;


import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "money_records")
public class MoneyRecord {
    //レコードID
    @Column(name = "record_id")
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //金額
    @NotNull
    @Column(name = "price")
    private int price;

    //支払い金額（自分）
    @NotNull
    @Column(name = "own_payment")
    private int ownPayment;

    //支払い金額（相手）
    @NotNull
    @Column(name = "partner_payment")
    private int partnerPayment;

    //備考
    @Length(min=0, max=20)
    @Column(name = "record_note")
    private String recordNote;

    //指定日付
    @Column(name = "date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull
    private LocalDate date;

    //レコード作成日
    @Column(name = "created_at")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime createdAt;

    //レコード更新日
    @Column(name = "updated_at")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    //レコード作成者
    @Column(name = "u_id")
    @NotNull
    private int userId;

    //カテゴリ番号
    @Column(name = "c_id")
    @NotNull
    private int categoryId;

    //支払い者
    @Column(name = "payer_id")
    @NotNull
    private int payerId;
}
