package com.itmaxglobal.billing.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itmaxglobal.billing.model.ModelType;
import com.itmaxglobal.billing.model.Operator;
import com.itmaxglobal.billing.model.deserializer.ModelTypeDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BillingStatusRequestDTO {

    private String imei;
    private String imsi;
    private String msisdn;
    @JsonDeserialize(using = ModelTypeDeserializer.class)
    private ModelType modelType;
    private Operator operator;
    private String dateTobeUpdate;

}
