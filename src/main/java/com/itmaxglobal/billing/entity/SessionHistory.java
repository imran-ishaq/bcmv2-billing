package com.itmaxglobal.billing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "session_history")
public class SessionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "imei",  length = 17)
    private String imei;

    @Column(name = "imsi")
    private Long imsi;

    @Column(name = "msisdn",  length = 32)
    private String msisdn;

    @Column(name = "created_date", updatable=false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;
}
