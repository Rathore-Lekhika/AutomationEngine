package com.jsp.AutomationEngine.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeExectionResult {
   private  boolean executionResult;
    private String executionStatus;
    private String remarks;

    @Override
    public NodeExectionResult clone() {
        try {
            NodeExectionResult clone = (NodeExectionResult) super.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
