package com.itmaxglobal.billing.model.deserializer;

import com.itmaxglobal.billing.model.DeviceStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeviceStatusConverter implements AttributeConverter<DeviceStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DeviceStatus deviceStatus) {
        if (deviceStatus == null) {
            return null;
        }
        return deviceStatus.getCode();
    }

    @Override
    public DeviceStatus convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return DeviceStatus.valueOf(dbData);
    }
}
