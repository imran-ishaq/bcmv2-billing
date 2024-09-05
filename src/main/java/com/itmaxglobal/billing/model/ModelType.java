package com.itmaxglobal.billing.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ModelType {
    OTHER("Other", "Other"), TWO_G("2G", "2G"),
    THREE_G("3G", "3G-4G+"), FOUR_G("4G", "3G-4G+"),
    FIVE_G("5G", "4G-5G+");

    private String code;
    private String category;

    ModelType(String code, String category)
    {
        this.code = code;
        this.category = category;
    }

    @JsonValue
    public String getCode()
    {
        return code;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public static ModelType valueOfCode(String code)
    {
        return Stream.of(ModelType.values()).filter(modelType -> modelType.getCode().equals(code)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

}
