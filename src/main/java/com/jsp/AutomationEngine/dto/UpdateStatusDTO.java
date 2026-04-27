package com.jsp.AutomationEngine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusDTO {
    private String workFlowCode;
    private String workFlowId;
    private String statusFlag;
}
