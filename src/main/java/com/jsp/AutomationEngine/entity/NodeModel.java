
package com.jsp.AutomationEngine.entity;


import com.jsp.AutomationEngine.service.ListToJsonConverter;
import com.jsp.AutomationEngine.service.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sa_wf_node")
public class NodeModel extends WorkFlowData {
    @Id

    @Column(name = "alt_key")
    private BigInteger altKey=generateAltKey();
    @Column(name = "workflow_id")
    private String workFlowId;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "node_type")
    private String nodeType;
    @Column(name = "node_id")
    private String nodeId;

    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "outgoing_nodes", columnDefinition = "TEXT")
    private List<String> outgoingNodes;
    @Convert(converter = ListToJsonConverter.class)
    @Column(name = "incoming_nodes", columnDefinition = "TEXT")
    private List<String> incomingNodes;

    @Convert(converter = MapToJsonConverter.class)
    @Column(name = "node_properties", columnDefinition = "TEXT")
    private Map<String, String> nodeProperties;


    public BigInteger generateAltKey() {
        return new BigInteger(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE) + "");
    }
}