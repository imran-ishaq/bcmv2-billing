package com.itmaxglobal.billing.model;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Operator {

    vodacom(1, "VDC", "Vodacom","63001"),
    Vodacom(1, "VDC", "Vodacom","63001"),

    orange(2, "ORG", "Orange","6308"),
    Orange(2, "ORG", "Orange","6308"),

    airtel(3, "ATL", "Airtel","63002"),
    Airtel(3, "ATL", "Airtel","63002"),

    africell(4, "AFC", "Africell","63090"),
    Africell(4, "AFC", "Africell","63090"),

    other(0,"other", "","x"),
    Other(0,"other", "","x");

    private final Integer code;
    private final String mno;
    private final String description;
    private final String imsiPrefix;

    Operator(Integer code, String mno, String description, String imsiPrefix)
    {
        this.code=code;
        this.mno=mno;
        this.description=description;
        this.imsiPrefix=imsiPrefix;
    }

    public static Operator valueOfCode(Integer code)
    {
        return Stream.of(Operator.values()).filter(operator -> operator.getCode().equals(code)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public static Operator valueOfMno(String mno)
    {
        return Stream.of(Operator.values()).filter(operator -> operator.getMno().equals(mno)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}