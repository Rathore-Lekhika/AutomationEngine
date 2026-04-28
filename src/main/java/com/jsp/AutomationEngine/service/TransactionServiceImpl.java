package com.jsp.AutomationEngine.service;

import com.jsp.AutomationEngine.context.NodeExecutionContext;
import com.jsp.AutomationEngine.context.WorkFlowTransactionContext;
import com.jsp.AutomationEngine.context.WorkFlowTransactionContext;
import com.jsp.AutomationEngine.entity.NodeConfig;
import com.jsp.AutomationEngine.entity.WorkFlowModel;
import com.jsp.AutomationEngine.entity.WorkFlowTransaction;
import com.jsp.AutomationEngine.entity.WorkFlowTransactionLog;
import com.jsp.AutomationEngine.repository.TransactionLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    private static final Map<String, Supplier<NodeExecutionService>> nodeMap = new HashMap<>();

    static {
        nodeMap.put("STARTEVENT", StartNode::new);
        nodeMap.put("ENDEVENT", EndNode::new);
    }


    // Entry method
    public void createTransactionContext(WorkFlowTransactionContext context) {
        try {
            execute(context);
        } catch (Exception e) {
            log.error("Error while executing workflow", e);
            throw e;
        }
    }

    public void execute(WorkFlowTransactionContext context) {

        WorkFlowModel workFlowModel = context.getWorkFlowModel();

        List<NodeConfig> startNodes = workFlowModel.getStartNodes();   // ✅ fixed typo
        List<NodeConfig> otherNodes = workFlowModel.getOtherNodes();

        if (context.getCurrentNodeExecutionContextMap() == null) {
            context.setCurrentNodeExecutionContextMap(new HashMap<>());
        }

        // ✅ Execute only START nodes
        if (startNodes != null && !startNodes.isEmpty()) {

            for (NodeConfig node : startNodes) {

                NodeExecutionContext nodeExecutionContext = new NodeExecutionContext();

                nodeExecutionContext.setCurrentNodeConfig(node);
                nodeExecutionContext.setWorkFlowModel(workFlowModel);
                nodeExecutionContext.setWorkflowTransactionEntity(context.getWorkflowTransactionEntity());

                nodeExecutionContext.setTransactionDataMap(new HashMap<>());
                nodeExecutionContext.setExecutionStatus("STARTED");
                nodeExecutionContext.setExecutionStart(new Date());

                nodeExecutionContext.setPreviousExecutedNodeConfig(null);
                nodeExecutionContext.setNextExecutedNodeConfig(
                        node.getOutgoingNode() != null ? node.getOutgoingNode() : new ArrayList<>()
                );

                context.addNodeExecutionContext(node.getNodeId(), nodeExecutionContext);

                executeNode(nodeExecutionContext, context);
            }
        }

        // ❌ DO NOT execute other nodes here
        // Just initialize them
        if (otherNodes != null && !otherNodes.isEmpty()) {
            for (NodeConfig node : otherNodes) {

                NodeExecutionContext nodeExecutionContext = new NodeExecutionContext();

                nodeExecutionContext.setCurrentNodeConfig(node);
                nodeExecutionContext.setWorkFlowModel(workFlowModel);
                nodeExecutionContext.setWorkflowTransactionEntity(context.getWorkflowTransactionEntity());

                nodeExecutionContext.setTransactionDataMap(new HashMap<>());
                nodeExecutionContext.setExecutionStatus("NOT_STARTED");

                nodeExecutionContext.setNextExecutedNodeConfig(
                        node.getOutgoingNode() != null ? node.getOutgoingNode() : new ArrayList<>()
                );

                context.getCurrentNodeExecutionContextMap()
                        .put(node.getNodeId(), nodeExecutionContext);
            }
        }
    }

    private void executeNode(NodeExecutionContext nodeExecutionContext,
                             WorkFlowTransactionContext context) {

        NodeExecutionService node = getNode(
                nodeExecutionContext.getCurrentNodeConfig().getNodeType()
        );

        uploadTxLog(nodeExecutionContext);

        log.info("Executing node: {}",
                nodeExecutionContext.getCurrentNodeConfig().getNodeType());

        node.process(context, nodeExecutionContext);
    }

    public void uploadTxLog(NodeExecutionContext context) {

        WorkFlowTransactionLog logEntity = new WorkFlowTransactionLog();

        logEntity.setAltKey(generateAltKey());

        if (context.getWorkflowTransactionEntity() != null) {
            logEntity.setTransactionId(
                    context.getWorkflowTransactionEntity().getTransactionId()
            );
            logEntity.setTransactionEndDate(
                    context.getWorkflowTransactionEntity().getTransactionEndDate()
            );
            logEntity.setTransactionUniqueValue(

                    context.getWorkflowTransactionEntity().getTransactionUniqueValue()
            );
        }

        logEntity.setNodeId(context.getCurrentNodeConfig().getNodeId());
        logEntity.setNodeType(context.getCurrentNodeConfig().getNodeType());
        logEntity.setWorkFlowId(context.getWorkFlowModel().getWorkFlowId());

        logEntity.setTransactionStartDate(new Date());

        if (context.getPreviousExecutedNodeConfig() != null) {
            logEntity.setPreviousNodeId(
                    context.getPreviousExecutedNodeConfig().getNodeId()
            );
        }

        logEntity.setStatusFlag("IN_PROGRESS");

        transactionLogRepository.save(logEntity);
    }

    public NodeExecutionService getNode(String nodeType) {

        log.info("Fetching node type: {}", nodeType);

        Supplier<NodeExecutionService> supplier = nodeMap.get(nodeType);

        if (supplier == null) {
            throw new IllegalArgumentException("Unsupported node type: " + nodeType);
        }

        return supplier.get();
    }

    public BigInteger generateAltKey() {
        return BigInteger.valueOf(System.currentTimeMillis()); // safer
    }


    @Override
    public void execute(WorkFlowTransaction context) {

    }
}