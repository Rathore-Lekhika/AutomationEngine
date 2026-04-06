package com.jsp.AutomationEngine.entity;





import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.Date;

@Data
@MappedSuperclass
public class WorkFlowData {

    @Column(name = "created_date")
    private Date createdDate = new Date();

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;

}

