package com.soen343.project.database.schema;

import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class CreateDatabase {

    private final boolean createTable;

    public CreateDatabase(boolean createTable) {
        this.createTable = createTable;
    }

    @PostConstruct
    public void createTable() {
        if (createTable) {
            ScriptUtils.executeSqlScript(null, (Resource) null);
        }
    }

}
