package com.jsp.AutomationEngine.repository;

import com.jsp.AutomationEngine.entity.WorkFlowTransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface TransactionLogRepository extends JpaRepository<WorkFlowTransactionLog, BigInteger> {
}
