package com.jsp.AutomationEngine.service;


import com.jsp.AutomationEngine.entity.NodeModel;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class BPMNHandler extends DefaultHandler {

    private List<NodeModel> nodes = new ArrayList<>();
    private Map<String, NodeModel> nodeMap = new HashMap<>();
    private List<String[]> sequenceFlows = new ArrayList<>();

    private NodeModel currentNode;

    private String workflowCode;
    private String tenantId;

    public BPMNHandler(String workflowCode, String tenantId) {
        this.workflowCode = workflowCode;
        this.tenantId = tenantId;
    }

    public List<NodeModel> getNodes() {
        return nodes;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        switch (qName) {

            // ✅ ALL NODE TYPES
            case "bpmn:startEvent":
            case "bpmn:endEvent":
            case "bpmn:task":
            case "bpmn:exclusiveGateway":
            case "bpmn:parallelGateway":
            case "bpmn:inclusiveGateway":

                currentNode = new NodeModel();

                currentNode.setWorkFlowId(workflowCode);
                currentNode.setTenantId(tenantId);

                currentNode.setNodeId(attributes.getValue("id"));
                currentNode.setNodeType(qName.replace("bpmn:", "").toUpperCase());

                currentNode.setNodeProperties(new HashMap<>());
                currentNode.setIncomingNodes(new ArrayList<>());
                currentNode.setOutgoingNodes(new ArrayList<>());

                if (attributes.getValue("name") != null) {
                    currentNode.getNodeProperties().put("name", attributes.getValue("name"));
                }

                break;

            // ✅ SEQUENCE FLOW
            case "bpmn:sequenceFlow":

                sequenceFlows.add(new String[]{
                        attributes.getValue("sourceRef"),
                        attributes.getValue("targetRef")
                });

                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (qName.equals("bpmn:startEvent") ||
                qName.equals("bpmn:endEvent") ||
                qName.equals("bpmn:task") ||
                qName.equals("bpmn:exclusiveGateway") ||
                qName.equals("bpmn:parallelGateway") ||
                qName.equals("bpmn:inclusiveGateway")) {

            nodes.add(currentNode);
            nodeMap.put(currentNode.getNodeId(), currentNode);
            currentNode = null;
        }
    }

    @Override
    public void endDocument() {

        // ✅ STEP 1: CONNECT NODES USING FLOWS
        for (String[] flow : sequenceFlows) {

            String source = flow[0];
            String target = flow[1];

            NodeModel sourceNode = nodeMap.get(source);
            NodeModel targetNode = nodeMap.get(target);

            if (sourceNode != null) {
                sourceNode.getOutgoingNodes().add(target);
            }

            if (targetNode != null) {
                targetNode.getIncomingNodes().add(source);
            }
        }

        // ✅ STEP 2: BUILD FINAL PROPERTIES
        for (NodeModel node : nodes) {

            Map<String, String> props = new HashMap<>();

            props.put("name", node.getNodeProperties().getOrDefault("name", ""));
            props.put("type", node.getNodeType());

            props.put("incoming", String.join(",", node.getIncomingNodes()));
            props.put("outgoing", String.join(",", node.getOutgoingNodes()));

            // ✅ CUSTOM LOGIC
            if ("TASK".equals(node.getNodeType())) {
                props.put("actionType", "SEND_SMS");
            }

            if ("EXCLUSIVEGATEWAY".equals(node.getNodeType())) {
                props.put("gatewayType", "DECISION");
            }

            if ("STARTEVENT".equals(node.getNodeType())) {
                props.put("isStart", "true");
            }

            if ("ENDEVENT".equals(node.getNodeType())) {
                props.put("isEnd", "true");
            }

            node.setNodeProperties(props);
        }
    }
}

