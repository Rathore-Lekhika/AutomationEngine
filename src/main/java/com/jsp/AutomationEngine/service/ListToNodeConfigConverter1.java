package com.jsp.AutomationEngine.service;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsp.AutomationEngine.entity.NodeConfig;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class ListToNodeConfigConverter1 implements AttributeConverter<List<NodeConfig>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<NodeConfig> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting List<NodeConfig> to JSON", e);
        }
    }

    @Override
    public List<NodeConfig> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return mapper.readValue(dbData, new TypeReference<List<NodeConfig>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to List<NodeConfig>", e);
        }
    }
}
