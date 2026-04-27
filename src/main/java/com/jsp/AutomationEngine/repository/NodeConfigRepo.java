package com.jsp.AutomationEngine.repository;

import com.jsp.AutomationEngine.entity.NodeConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NodeConfigRepo extends JpaRepository<NodeConfig,String> {

    public NodeConfig findByNodeIdAndIsStartNodeTrue(String nodeId);

    public List<NodeConfig> findByIsStartNodeFalse();
}
