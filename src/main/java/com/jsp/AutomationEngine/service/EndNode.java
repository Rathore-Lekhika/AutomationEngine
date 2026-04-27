package com.jsp.AutomationEngine.service;

import com.jsp.AutomationEngine.context.NodeExecutionContext;
import com.jsp.AutomationEngine.context.WorkFlowTransactionContext;
import org.springframework.stereotype.Service;


@Service
public class EndNode extends NodeExecutionImpl {
    @Override
    public void process(WorkFlowTransactionContext txContext, NodeExecutionContext executionContext) {

    }
}
