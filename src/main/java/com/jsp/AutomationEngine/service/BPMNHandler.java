package com.jsp.AutomationEngine.service;


import com.jsp.AutomationEngine.entity.NodeModel;


import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BPMNHandler extends DefaultHandler {
    private List<NodeModel> nodes = new ArrayList<>();
    private Map<String, NodeModel> nodeMap = new HashMap<>();

    private NodeModel currentNode;
    private String currentElement;

    public List<NodeModel> getNodes() {
        return nodes;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        switch (qName) {

            case "bpmn:startEvent":
            case "bpmn:endEvent":
            case "bpmn:task":

                currentNode = new NodeModel();
                currentNode.setAltKey(BigInteger.valueOf(System.nanoTime()));
                currentNode.setNode_ID(attributes.getValue("id"));
                currentNode.setNodeType(qName.replace("bpmn:", "").toUpperCase());
                currentNode.setNodeProperties(new HashMap<>());
                currentNode.setIncomingNodes(new ArrayList<>());
                currentNode.setOutgoingNodes(new ArrayList<>());

                if (attributes.getValue("name") != null) {
                    currentNode.getNodeProperties().put("name", attributes.getValue("name"));
                }

                break;

            case "bpmn:sequenceFlow":

                String source = attributes.getValue("sourceRef");
                String target = attributes.getValue("targetRef");

                NodeModel sourceNode = nodeMap.get(source);
                NodeModel targetNode = nodeMap.get(target);

                if (sourceNode != null) {
                    sourceNode.getOutgoingNodes().add(target);
                }

                if (targetNode != null) {
                    targetNode.getIncomingNodes().add(source);
                }

                break;

            case "bpmn:incoming":
            case "bpmn:outgoing":
                currentElement = qName;
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (currentNode == null || currentElement == null) return;

        String value = new String(ch, start, length).trim();
        if (value.isEmpty()) return;

        if (currentElement.equals("bpmn:incoming")) {
            currentNode.getIncomingNodes().add(value);
        }

        if (currentElement.equals("bpmn:outgoing")) {
            currentNode.getOutgoingNodes().add(value);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {

        if (qName.equals("bpmn:startEvent") ||
                qName.equals("bpmn:endEvent") ||
                qName.equals("bpmn:task")) {

            // ✅ Build JSON properties here
            Map<String, String> props = new HashMap<>();

            props.put("name", currentNode.getNodeProperties().get("name"));
            props.put("type", currentNode.getNodeType());

            // Convert list → comma separated string
            props.put("incoming", String.join(",", currentNode.getIncomingNodes()));
            props.put("outgoing", String.join(",", currentNode.getOutgoingNodes()));

            // Example custom property
            if ("TASK".equals(currentNode.getNodeType())) {
                props.put("actionType", "SEND_EMAIL");
            }

            // ✅ Set final JSON map
            currentNode.setNodeProperties(props);

            nodes.add(currentNode);
            nodeMap.put(currentNode.getNode_ID(), currentNode);

            currentNode = null;
        }

        currentElement = null;
    }
}