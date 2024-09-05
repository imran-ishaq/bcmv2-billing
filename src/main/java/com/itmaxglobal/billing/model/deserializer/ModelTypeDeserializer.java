package com.itmaxglobal.billing.model.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.itmaxglobal.billing.model.ModelType;

import java.io.IOException;

public class ModelTypeDeserializer extends JsonDeserializer<ModelType> {
    @Override
    public ModelType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String code = jsonParser.getText();
        return switch (code) {
            case "2G", "TWO_G" -> ModelType.TWO_G;
            case "3G", "THREE_G" -> ModelType.THREE_G;
            case "4G", "FOUR_G" -> ModelType.FOUR_G;
            case "5G", "FIVE_G" -> ModelType.FIVE_G;
            case "Other", "OTHER" -> ModelType.OTHER;
            default -> throw new IllegalArgumentException("Unknown model type: " + code);
        };
    }
}
