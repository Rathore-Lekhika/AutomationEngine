package com.jsp.AutomationEngine.config;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class AppConstants {
    // DB ENV VARIABLES
    public static final String DB_URL = "DB_URL";
    public static final String DB_USERNAME = "DB_USERNAME";
    public static final String DB_PASSWORD = "DB_PASSWORD";

    // DRIVER
    public static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";

    // PACKAGE
    public static final String BASE_PACKAGE = "com.jsp.AutomationEngine";

    // HIBERNATE
    public static final String HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
    public static final String HBM2DDL_UPDATE = "update";
}
