package com.jsp.AutomationEngine.repository;

import com.jsp.AutomationEngine.entity.NodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<NodeModel, BigInteger>{

    public List<NodeModel> findByWorkFlowIdAndTenantId(String wfId,String tId);
    public List<NodeModel> findByWorkFlowId(String wfId);
}






