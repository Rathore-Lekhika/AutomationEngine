package com.jsp.AutomationEngine.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Data
@Entity
@NoArgsConstructor
@Table(name = "sa_wf_transaction")
public class WorkFlowTransaction {
    @Id
    @Column(name = "alt_key")
    private BigInteger altKey= generatealtkey();

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "transaction_uniquevalue")
    private String transactionUniqueValue;

    @Column(name = "workflowcode")
    private String  workFlowCode;

    @Column(name="workflowid")
    private String workFlowId;

    @Column(name = "status_flag")
    private  String statusFlag;



    @Column(name = "tenant_id")
    private String tenantId;



    @Column(name = "transactionstart_date")
    private Date transactionStartDate;

    @Column(name = "transactionend_date")
    private Date transactionEndDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_date")
    private Date createdDate ;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "createdby_date")
    private Date createdByDate ;

    @Column(name = "modifiedby_date")
    private Date modifiedByDate;

    private BigInteger generatealtkey() {

        long value = Math.abs(ThreadLocalRandom.current().nextLong());
        return BigInteger.valueOf(value);

    }
    @Override
    public WorkFlowTransaction clone() {
        try {
            WorkFlowTransaction clone = (WorkFlowTransaction) super.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}

