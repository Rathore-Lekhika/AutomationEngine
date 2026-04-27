package com.jsp.AutomationEngine.context;


import com.jsp.AutomationEngine.entity.NodeConfig;
import com.jsp.AutomationEngine.entity.WorkFlowModel;
import com.jsp.AutomationEngine.entity.WorkFlowTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class NodeExecutionContext implements Cloneable {
        private String executionStatus;
        private NodeConfig currentNodeConfig;
        private NodeConfig previousExecutedNodeConfig;
        private List<NodeConfig> nextExecutedNodeConfig;
        private Date executionStart;
        private Date executionEnd;
        private WorkFlowModel workFlowModel;
        private WorkFlowTransaction workflowTransactionEntity;
        private Map<String,Object> transactionDataMap;
        private NodeExectionResult nodeExectionResult;

        @Override
        public NodeExecutionContext clone() {
            try {
                NodeExecutionContext clone = (NodeExecutionContext) super.clone();

                clone.currentNodeConfig = this.currentNodeConfig != null ? this.currentNodeConfig.clone() : null;
                clone.previousExecutedNodeConfig = this.previousExecutedNodeConfig != null ? this.previousExecutedNodeConfig.clone() : null;

                if (this.nextExecutedNodeConfig != null) {
                    clone.nextExecutedNodeConfig = new ArrayList<>();
                    for (NodeConfig node : this.nextExecutedNodeConfig) {
                        clone.nextExecutedNodeConfig.add(node != null ? node.clone() : null);
                    }
                }

                clone.executionStart = this.executionStart != null ? (Date) this.executionStart.clone() : null;
                clone.executionEnd = this.executionEnd != null ? (Date) this.executionEnd.clone() : null;

                clone.workFlowModel = this.workFlowModel != null ? this.workFlowModel.clone() : null;
                clone.workflowTransactionEntity = this.workflowTransactionEntity != null
                        ? this.workflowTransactionEntity.clone() : null;

                clone.transactionDataMap = this.transactionDataMap != null
                        ? new HashMap<>(this.transactionDataMap)
                        : null;
                clone.nodeExectionResult = this.nodeExectionResult != null
                        ? this.nodeExectionResult.clone()
                        : null;

                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
}
