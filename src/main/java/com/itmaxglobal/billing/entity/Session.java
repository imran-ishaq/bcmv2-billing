package com.itmaxglobal.billing.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itmaxglobal.billing.model.DeviceStatus;
import com.itmaxglobal.billing.model.deserializer.DeviceStatusConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_new_2")
@Getter
@Setter
@ToString
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "imei")
    private String imei;

    @Column(name = "imsi")
    private Long imsi;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "model_type")
    private String modelType;

    @Column(name = "counterfeit")
    private Integer counterfeit;

    @Column(name = "created_date", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "registering_date")
    private LocalDateTime registeringDate;

    @JsonIgnore
    @Column(name = "updated_date")
    private LocalDateTime updatedAt;

    @Column(name = "roaming")
    private Integer roaming;

    @Column(name = "brand_model_id")
    private Integer brandModelId;

    @Column(name = "imei_quantity_support")
    private Integer imeiQuantitySupport;
//
//    @Column(name = "brand", length = 500)
//    private String brand;
//
//    @Column(name = "model", length = 500)
//    private String model;

    @Column(name = "sim_swap_counter")
    private Integer simSwapCounter;

    @Column(name = "operator")
    private Integer operator;

    @Column(name = "blocked")
    private Boolean isStolen;

    @Column(name = "account_status")
    @Convert(converter = DeviceStatusConverter.class)
    private DeviceStatus imeiStatus;

    @Column(name = "status_update_date")
    private LocalDateTime statusUpdateDate;

    @Column(name = "is_cloned")
    private Boolean isCloned;

    @Column(name = "account_operator")
    private Integer accountOperator;

    @Column(name = "last_activity_date")
    private LocalDateTime lastActivityDate;

}
