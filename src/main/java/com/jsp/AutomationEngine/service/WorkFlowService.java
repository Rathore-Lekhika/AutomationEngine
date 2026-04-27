package com.jsp.AutomationEngine.service;

import com.jsp.AutomationEngine.dto.AppResponseDTO;
import com.jsp.AutomationEngine.dto.UpdateStatusDTO;
import com.jsp.AutomationEngine.dto.WorkFlowDTO;
import com.jsp.AutomationEngine.entity.NodeModel;
import com.jsp.AutomationEngine.entity.WorkFlowModel;

import java.util.List;

public interface WorkFlowService {

    AppResponseDTO processSaveUpload(List<WorkFlowDTO> dto);

    AppResponseDTO processUpdateStatus(UpdateStatusDTO dto);

}
