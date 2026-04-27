package com.jsp.AutomationEngine.controller;

import com.jsp.AutomationEngine.context.WorkFlowTransactionContext;
import com.jsp.AutomationEngine.dto.AppResponseDTO;
import com.jsp.AutomationEngine.dto.UpdateStatusDTO;
import com.jsp.AutomationEngine.dto.WorkFlowDTO;
import com.jsp.AutomationEngine.entity.WorkFlowModel;
import com.jsp.AutomationEngine.entity.WorkFlowTransaction;
import com.jsp.AutomationEngine.service.TransactionService;
import com.jsp.AutomationEngine.service.WorkFlowService;
import com.jsp.AutomationEngine.service.WorkFlowServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workflow")
public class WorkFlowController {

    public static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowController.class);
    @Autowired
    WorkFlowServiceImpl service;

    @PostMapping(value = "/saveUpload")
    public AppResponseDTO saveUpload(@RequestBody List<WorkFlowDTO> dto) {

        return service.processSaveUpload(dto);
    }
    @PostMapping(value = "/updateStatus")
    public AppResponseDTO updateStatus(@RequestBody UpdateStatusDTO dto){

        return service.processUpdateStatus(dto);
    }
}