package com.example.mappingvaluetypes.converter;

import com.example.mappingvaluetypes.model.MonetaryAmount;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {
    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        return attribute.toString();
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        return MonetaryAmount.fromString(dbData);
    }
}
