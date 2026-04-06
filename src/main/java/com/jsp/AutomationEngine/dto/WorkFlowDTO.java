package com.jsp.AutomationEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkFlowDTO {

    private String  workFlowCode;
    private String workFlowName;
    private  String uniqueField;
    private String sourceData;
    private String tenantId;
    private  String entityCode;
}
