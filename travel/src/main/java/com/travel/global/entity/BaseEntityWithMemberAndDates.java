package com.travel.global.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;

public class BaseEntityWithMemberAndDates extends BaseEntityWithModifiedDate{

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;
}
