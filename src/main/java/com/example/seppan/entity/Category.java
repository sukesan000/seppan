package com.example.seppan.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Column(name = "category_id")
    @Id
    private int id;

    @Column(name = "name")
    private String name;
}
