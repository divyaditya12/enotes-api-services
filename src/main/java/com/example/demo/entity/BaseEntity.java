package com.example.demo.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class BaseEntity {

    @CreatedBy
    @Column(updatable = false)
    private Integer createdBy;
    @CreatedDate
    @Column(updatable = false)
    private Date createdOn;
    @LastModifiedBy
    @Column(insertable = false)
    private Integer updatedBy;
    @LastModifiedDate
    @Column(insertable = false)
    private Date updatedOn;
}
