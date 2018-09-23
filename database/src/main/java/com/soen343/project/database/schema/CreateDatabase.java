package com.soen343.project.database.schema;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.soen343.project.database.DatabaseConstants.*;

/**
 * Created by Kevin Tan 2018-09-23
 */

public class CreateDatabase {

    private final boolean createTable;
    private final List<Resource> dbScripts = new LinkedList<>();

    public CreateDatabase(boolean createTable) {
        this.createTable = createTable;
        File directory = new File(SCRIPT_DIRECTORY);
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            dbScripts.add(new ClassPathResource(file.getAbsolutePath()));
        }
    }

    @PostConstruct
    public void createTable() {
        //Requires connection and Script for resource
        if (createTable) {
            dbScripts.forEach(resource -> ScriptUtils.executeSqlScript(null, resource));
        }
    }

}
