package com.jsp.AutomationEngine.service;

import com.jsp.AutomationEngine.context.WorkFlowTransactionContext;
import com.jsp.AutomationEngine.entity.WorkFlowTransaction;

public interface TransactionService {
    void execute(WorkFlowTransaction context);
}
