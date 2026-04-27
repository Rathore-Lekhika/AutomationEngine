package com.jsp.AutomationEngine.service;



import com.jsp.AutomationEngine.context.WorkFlowTransactionContext;
import com.jsp.AutomationEngine.dto.AppResponseDTO;
import com.jsp.AutomationEngine.dto.UpdateStatusDTO;
import com.jsp.AutomationEngine.dto.WorkFlowDTO;
import com.jsp.AutomationEngine.entity.NodeConfig;
import com.jsp.AutomationEngine.entity.NodeModel;
import com.jsp.AutomationEngine.entity.WorkFlowModel;
import com.jsp.AutomationEngine.entity.WorkFlowTransaction;
import com.jsp.AutomationEngine.repository.NodeConfigRepo;
import com.jsp.AutomationEngine.repository.NodeRepository;
import com.jsp.AutomationEngine.repository.TransactionRepository;
import com.jsp.AutomationEngine.repository.WorkFlowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class WorkFlowServiceImpl implements WorkFlowService {


    @Autowired
    WorkFlowRepository repo;
    Logger logger = LoggerFactory.getLogger(WorkFlowServiceImpl.class);
    @Autowired
    NodeRepository nodeRepo;
    @Autowired
    NodeConfigBuilder nodeConfigBuilder;
    @Autowired
    NodeConfigRepo nodeConfigRepo;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionServiceImpl transactionService;
    @Override
    public AppResponseDTO processSaveUpload(List<WorkFlowDTO> dto) {
        try {

            List<WorkFlowModel> collect = dto.stream().map(modeldata -> {

                Optional<WorkFlowModel> exist = repo.findByWorkFlowCodeAndStatusFlag(modeldata.getWorkFlowCode(), "DRAFT");
                WorkFlowModel workFlowModel;
                if (exist.isPresent()) {
                    workFlowModel = exist.get();
                    workFlowModel.setSourceData(modeldata.getSourceData());
                    workFlowModel.setTenantId(modeldata.getTenantId());
                    workFlowModel.setWorkFlowName(modeldata.getWorkFlowName());
                    workFlowModel.setUniqueField(modeldata.getUniqueField());
                    workFlowModel.setEntityCode(modeldata.getEntityCode());
                } else {

                    workFlowModel = new WorkFlowModel();
                    workFlowModel.setAltKey(generateAltKey());
                    workFlowModel.setWorkFlowCode(modeldata.getWorkFlowCode());
                    workFlowModel.setSourceData(modeldata.getSourceData());
                    workFlowModel.setTenantId(modeldata.getTenantId());
                    workFlowModel.setWorkFlowName(modeldata.getWorkFlowName());
                    workFlowModel.setUniqueField(modeldata.getUniqueField());
                    workFlowModel.setEntityCode(modeldata.getEntityCode());
                    workFlowModel.setWorkFlowId(modeldata.getWorkFlowCode() + "_" + workFlowModel.getWorkFlowVersion());
                }
                return workFlowModel;
            }).collect(Collectors.toList());


            return new AppResponseDTO("200", null, "Sucess", repo.saveAll(collect));
        } catch (Exception e) {
            return new AppResponseDTO("500", e.getMessage(), "failure", null);
        }
    }

    @Transactional
    @Override
    public AppResponseDTO processUpdateStatus(UpdateStatusDTO dto) {

        try {
            WorkFlowModel active = repo.findWithLock(dto.getWorkFlowCode(), "ACTIVE");
            if (active != null) {
                active.setStatusFlag("INACTIVE");
                repo.save(active);
            }
            WorkFlowModel draft = repo.findWithLock(dto.getWorkFlowCode(), "DRAFT");
            draft.setStatusFlag("ACTIVE");
            BigInteger bigInteger = repo.maxVersion(dto.getWorkFlowCode());
            logger.debug(" maxVersion:{}", bigInteger);
            draft.setWorkFlowVersion(Integer.valueOf(String.valueOf(bigInteger)) + 1);
            draft.setWorkFlowId(draft.getWorkFlowCode() + "_" + draft.getWorkFlowVersion());
            repo.save(draft);

            List<NodeModel> nodeList = parse(draft.getSourceData(), draft.getWorkFlowId(), draft.getTenantId());
            nodeRepo.saveAll(nodeList);


            WorkFlowModel flowModel = getWorkflowbyIdandtId(draft.getWorkFlowId(), draft.getTenantId());
            List<NodeConfig> nodeConfig = nodeConfigBuilder.getNodeConfig(flowModel.getNodeProperties());
            nodeConfigRepo.saveAll(nodeConfig);

            createTxEntity(draft);


            return new AppResponseDTO("200", null, "success", null);
        } catch (Exception e) {
            return new AppResponseDTO("500", e.getMessage(), "failure", null);
        }

    }

    public void createTxEntity(WorkFlowModel workFlowModel) {

        WorkFlowTransaction workflowTransactionEntity = new WorkFlowTransaction();
        workflowTransactionEntity.setAltKey(generateAltKey());
        workflowTransactionEntity.setWorkFlowCode(workFlowModel.getWorkFlowCode());
        workflowTransactionEntity.setTenantId(workFlowModel.getTenantId());
        workflowTransactionEntity.setStatusFlag("IN_PROGRESS");
        workflowTransactionEntity.setTransactionStartDate(new Date());
        workflowTransactionEntity.setWorkFlowId(workFlowModel.getWorkFlowId());

        transactionRepository.save(workflowTransactionEntity);

        // ✅ Create context object
        WorkFlowTransactionContext context = new WorkFlowTransactionContext();
        context.setWorkFlowModel(workFlowModel);
        context.setWorkflowTransactionEntity(workflowTransactionEntity);
        context.setExecutionStartDate(new Date()); // optional but useful

        // ✅ Correct method call
        transactionService.createTransactionContext(context);
    }

    public WorkFlowModel getWorkflowbyIdandtId(String wfId, String tId) {
        WorkFlowModel byWorkFlowIdAndTenantId = repo.findByWorkFlowIdAndTenantId(wfId, tId);
        List<NodeModel> nodeModelList = nodeRepo.findByWorkFlowIdAndTenantId(wfId, tId);
        byWorkFlowIdAndTenantId.setNodeProperties(nodeModelList);
         return byWorkFlowIdAndTenantId;
    }

    public BigInteger generateAltKey() {
        return new BigInteger(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) + "");
    }

    public List<NodeModel> parse(String xml, String workflowCode, String tenantId) {

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            BPMNHandler handler = new BPMNHandler(workflowCode, tenantId);

            saxParser.parse(new InputSource(new StringReader(xml)), handler);

            return handler.getNodes();

        } catch (Exception e) {
            throw new RuntimeException("Error parsing BPMN XML", e);
        }
    }
}