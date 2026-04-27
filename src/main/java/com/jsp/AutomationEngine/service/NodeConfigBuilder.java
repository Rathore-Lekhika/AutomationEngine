package com.jsp.AutomationEngine.service;


import com.jsp.AutomationEngine.entity.NodeConfig;
import com.jsp.AutomationEngine.entity.NodeModel;



import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class NodeConfigBuilder {
    public List<NodeConfig> getNodeConfig(List<NodeModel> list){
        Map<String,NodeConfig> configMap = new HashMap<>();
        for(NodeModel nodeModel:list){
            NodeConfig nodeConfig = new NodeConfig();
            nodeConfig.setNodeId(nodeModel.getNodeId());
            nodeConfig.setNodeType(nodeModel.getNodeType());
            nodeConfig.setNodeProperties(nodeModel.getNodeProperties());
            nodeConfig.setIsStartNode("STARTEVENT".equals(nodeModel.getNodeType()));
            nodeConfig.setIsEndNode("ENDEVENT".equals(nodeModel.getNodeType()));
            nodeConfig.setOutgoingNode(new ArrayList<>());
            nodeConfig.setIncomingNode(new ArrayList<>());
            configMap.put(nodeModel.getNodeId(),nodeConfig);
        }
        for (NodeModel nodeModel:list){
            NodeConfig configmap = configMap.get(nodeModel.getNodeId());
            List<String> outgoingNodes = nodeModel.getOutgoingNodes();
            if (outgoingNodes!=null){
                for(String outgoing : outgoingNodes){
                    NodeConfig nodeConfigMapOutgoing = configMap.get(outgoing);
                    if (nodeConfigMapOutgoing!=null){
                        configmap.getOutgoingNode().add(nodeConfigMapOutgoing);
                        nodeConfigMapOutgoing.getIncomingNode().add(configmap);
                    }
                }
            }
        }
        return new ArrayList<>(configMap.values());
    }
}
