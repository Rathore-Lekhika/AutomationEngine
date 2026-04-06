package com.jsp.AutomationEngine.service;

import com.jsp.AutomationEngine.dto.WorkFlowDTO;
import com.jsp.AutomationEngine.entity.WorkFlowModel;
import com.jsp.AutomationEngine.repository.WorkFlowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WorkFlowServiceImpl implements WorkFlowService{
  @Autowired
    private WorkFlowRepository workFlowRepository;



    @Override
    public WorkFlowModel saveWorkFlow(WorkFlowDTO dto) {


        WorkFlowModel entity= new WorkFlowModel();

        entity.setAltKey(generatealtkey());
        entity.setWorkFlowCode(dto.getWorkFlowCode());
        entity.setWorkFlowName(dto.getWorkFlowName());
        entity.setUniqueField(dto.getUniqueField());
        entity.setSourceData(dto.getSourceData());
        entity.setTenantId(dto.getTenantId());
        entity.setEntityCode(dto.getEntityCode());
        entity.setSourceData(dto.getSourceData());
        entity.setUniqueField(dto.getUniqueField());



        entity.setCreatedDate(new Date());
        entity.setStatusFlag("ACTIVE");

        return workFlowRepository.save(entity);
    }

    @Override
    public WorkFlowModel CreateWorkFlow(WorkFlowDTO dto) {
        Optional<WorkFlowModel> existing =workFlowRepository
                .findByWorkFlowCodeAndTenantIdAndStatusFlag(
                        dto.getWorkFlowCode(),
                        dto.getTenantId(),
                        "DRAFT"
                );

        if (existing.isPresent()) {
            throw new RuntimeException("Draft workflow already exists");
        }

else {
            WorkFlowModel entity = new WorkFlowModel();

            entity.setWorkFlowCode(dto.getWorkFlowCode());
            entity.setWorkFlowName(dto.getWorkFlowName());
            entity.setUniqueField(dto.getUniqueField());
            entity.setSourceData(dto.getSourceData());
            entity.setTenantId(dto.getTenantId());
            entity.setEntityCode(dto.getEntityCode());

            // default values
            entity.setStatusFlag("DRAFT");
            entity.setWorkFlowVersion(0);

            // 🔥 Generate workFlowId
            entity.setWorkFlowId(
                    dto.getWorkFlowCode() + "_" + entity.getWorkFlowVersion()
            );

            entity.setCreatedDate(new Date());

            return workFlowRepository.save(entity);
        }
    }

    @Override
    public WorkFlowModel activateWorkFlow(WorkFlowDTO dto) {
        String code = dto.getWorkFlowCode();
        String tenant = dto.getTenantId();



        // 🔍 Step 1: Check DRAFT exists
        Optional<WorkFlowModel> draftOpt =
              workFlowRepository.findByWorkFlowCodeAndStatusFlag(code, "DRAFT");

        // 🔍 Step 2: Get max version
        Integer maxVersion =   workFlowRepository.maxValue(code, tenant);
        int newVersion = (maxVersion == null) ? 0 : maxVersion + 1;

        WorkFlowModel entity;

        if (draftOpt.isPresent()) {
            // ✅ CASE 1: Draft Found → Convert to ACTIVE

            entity = draftOpt.get();

            entity.setStatusFlag("ACTIVE");
            entity.setWorkFlowVersion(newVersion);

            entity.setWorkFlowId(code + "_" + newVersion);

        } else {
            // ✅ CASE 2: No Draft → Create New ACTIVE

            entity = new WorkFlowModel();

            entity.setWorkFlowCode(dto.getWorkFlowCode());
            entity.setWorkFlowName(dto.getWorkFlowName());
            entity.setUniqueField(dto.getUniqueField());
            entity.setSourceData(dto.getSourceData());
            entity.setTenantId(dto.getTenantId());
            entity.setEntityCode(dto.getEntityCode());

            entity.setStatusFlag("ACTIVE");
            entity.setWorkFlowVersion(newVersion);

            entity.setWorkFlowId(code + "_" + newVersion);

            entity.setCreatedDate(new Date());
        }

        // 🔥 Step 3: Previous ACTIVE → INACTIVE
        List<WorkFlowModel> allWorkflows =
               workFlowRepository.findByWorkFlowCode(code);

        for (WorkFlowModel wf : allWorkflows) {
            if ("ACTIVE".equals(wf.getStatusFlag())
                    && !wf.getWorkFlowId().equals(entity.getWorkFlowId())) {

                wf.setStatusFlag("INACTIVE");
                workFlowRepository.save(wf);
            }
        }

        // 💾 Step 4: Save new/updated entity
        return  workFlowRepository.save(entity);
    }

    private BigInteger generatealtkey() {

        long value = Math.abs(ThreadLocalRandom.current().nextLong());
        return BigInteger.valueOf(value);

    }
}
