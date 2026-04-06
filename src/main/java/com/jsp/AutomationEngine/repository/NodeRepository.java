package com.jsp.AutomationEngine.repository;

import com.jsp.AutomationEngine.entity.NodeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface NodeRepository extends JpaRepository<NodeModel, BigInteger>{

}






