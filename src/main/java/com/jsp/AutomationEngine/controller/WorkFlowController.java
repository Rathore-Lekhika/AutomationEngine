package com.jsp.AutomationEngine.controller;

import com.jsp.AutomationEngine.dto.WorkFlowDTO;
import com.jsp.AutomationEngine.entity.WorkFlowModel;
import com.jsp.AutomationEngine.service.WorkFlowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflow")
public class WorkFlowController {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(WorkFlowController.class);

    @Autowired
    private WorkFlowService workFlowService;

    @PostMapping("/save")
    public WorkFlowModel save(@RequestBody WorkFlowDTO dto) {

        LOGGER.info("Received request to save workflow");
        LOGGER.debug("DTO Data: {}", dto);

        WorkFlowModel response = workFlowService.saveWorkFlow(dto);

        LOGGER.info("Workflow saved successfully with ID: {}", response.getAltKey());

        return response;
    }
    @PostMapping("/create")
    public WorkFlowModel create(@RequestBody WorkFlowDTO dto) {

        LOGGER.info("Creating workflow...");

        return workFlowService.CreateWorkFlow(dto);
    }

    @PostMapping("/activate")
    public WorkFlowModel activate(@RequestBody WorkFlowDTO dto) {
        return workFlowService.activateWorkFlow(dto);
    }
}