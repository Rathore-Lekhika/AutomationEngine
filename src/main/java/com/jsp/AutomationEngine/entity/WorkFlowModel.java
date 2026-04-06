package com.jsp.AutomationEngine.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@NoArgsConstructor
@Table(name = "work_flow" )
public class WorkFlowModel {
    @Id
    @Column(name = "alt_key")
    private BigInteger altKey= generatealtkey();
    @Column(name = "workflowversion")
    private Integer workFlowVersion;

    @Column(name ="tenant_id")
    private String tenantId;

    @Column(name = "workflowcode")
    private String  workFlowCode;

    @Column(name = "workflowname")
    private String workFlowName;

    @Column(name = "status_flag")
    private  String statusFlag;

    @Column(name = "uniquefield")
    private String uniqueField;

    @Column(name ="sourcedata")
    private String  sourceData;

    @Column(name = "created_date")
    private Date createdDate ;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "createdby_date")
    private Date createdByDate ;

    @Column(name = "modifiedby_date")
    private Date modifiedByDate;



    @Column(name="entitycode")
    private String entityCode;

    @Column(name="workflowid")
    private String workFlowId;

    private BigInteger generatealtkey() {

        long value = Math.abs(ThreadLocalRandom.current().nextLong());
        return BigInteger.valueOf(value);

    }
}
