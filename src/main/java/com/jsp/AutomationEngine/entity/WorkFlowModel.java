package com.jsp.AutomationEngine.entity;

import com.jsp.AutomationEngine.service.NodeConfigBuilder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sa_wf_model")
public class WorkFlowModel extends WorkFlowData {
    @Id
    @Column(name = "alt_key")
    private BigInteger altKey;
    @Column(name = "workflow_version")
    private Integer workFlowVersion=0;
    @Column(name = "entity_code")
    private String entityCode;
    @Column(name = "workflow_id")
    private String workFlowId;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "workflow_code")
    private String workFlowCode;
    @Column(name = "workflow_name")
    private String workFlowName;
    @Column(name = "status_flag")
    private String statusFlag="DRAFT";
    @Column(name = "unique_field")
    private String uniqueField;
    @Column(name = "source_data", columnDefinition = "LONGTEXT")
    private String sourceData;
    @Transient
    private List<NodeModel> nodeProperties;





    @Override
    public WorkFlowModel clone() {
        try {
            WorkFlowModel clone = (WorkFlowModel) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }


    public List<NodeConfig> getStartNodes() {
        return new NodeConfigBuilder().getNodeConfig(this.nodeProperties).stream().filter(each->each.getNodeType().equals("STARTEVENT"))
                .collect(Collectors.toList());
    }
    public List<NodeConfig> getOtherNodes(){
        return new NodeConfigBuilder().getNodeConfig(this.nodeProperties).stream().filter(each->!each.getNodeType().equals("STARTEVENT"))
                .collect(Collectors.toList());
    }
}
