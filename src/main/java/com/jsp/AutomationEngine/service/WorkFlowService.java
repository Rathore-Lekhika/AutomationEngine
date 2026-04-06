package com.jsp.AutomationEngine.service;

import com.jsp.AutomationEngine.dto.WorkFlowDTO;
import com.jsp.AutomationEngine.entity.WorkFlowModel;

public interface WorkFlowService {

    public WorkFlowModel saveWorkFlow(WorkFlowDTO dto);


        public WorkFlowModel  CreateWorkFlow(WorkFlowDTO dto);
    public WorkFlowModel activateWorkFlow(WorkFlowDTO dto);
}
