package com.jsp.AutomationEngine.repository;

import com.jsp.AutomationEngine.entity.WorkFlowModel;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional


public interface WorkFlowRepository extends JpaRepository<WorkFlowModel, BigInteger> {

    Optional<WorkFlowModel> findByWorkFlowCodeAndStatusFlag(String wf, String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WorkFlowModel w WHERE w.workFlowCode = :code AND w.statusFlag = :status")
    WorkFlowModel findWithLock(@Param("code") String code,
                               @Param("status") String status);

    @Query(value = "SELECT MAX(workflow_version) FROM workflow_model_master WHERE workflow_code = :workflowCode", nativeQuery = true)
    BigInteger maxVersion(@Param("workflowCode") String workflowCode);

    WorkFlowModel findByWorkFlowIdAndTenantId(String wfId, String tId);

    WorkFlowModel findByWorkFlowId(String wfId);
}