package com.jsp.AutomationEngine.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class AppResponseDTO {
        private String statusCode;
        private String errormessage;
        private String status;
        private Object data;
    }

