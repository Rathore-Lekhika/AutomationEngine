package com.jsp.AutomationEngine.repository;

import com.jsp.AutomationEngine.entity.WorkFlowModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional

public interface WorkFlowRepository extends JpaRepository<WorkFlowModel, BigInteger> {
    Optional<WorkFlowModel> findByWorkFlowCodeAndTenantIdAndStatusFlag(
            String workFlowCode,
            String tenantId,
            String statusFlag
    );

    Optional<WorkFlowModel> findByWorkFlowCodeAndStatusFlag(String workFlowCode, String statusFlag);

    @Query("SELECT MAX(w.workFlowVersion) FROM WorkFlowModel w WHERE w.workFlowCode = :code AND w.tenantId = :tenant")
    Integer maxValue(@Param("code") String code, @Param("tenant") String tenant);

    List<WorkFlowModel> findByWorkFlowCode(String workFlowCode);

}