package com.jsp.AutomationEngine.repository;

import com.jsp.AutomationEngine.entity.WorkFlowTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TransactionRepository extends JpaRepository<WorkFlowTransaction, BigInteger> {
}
