package com.soen343.project.database.schema;

import com.soen343.project.database.connection.DatabaseConnector;
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
        File create = new File(SCRIPT_CREATE_DIRECTORY);
        File data = new File(SCRIPT_DATA_DIRECTORY);
        for (File file : Objects.requireNonNull(create.listFiles())) {
            dbScripts.add(new ClassPathResource(CREATE_DIRECTORY + file.getName()));
        }
        for (File file : Objects.requireNonNull(data.listFiles())) {
            dbScripts.add(new ClassPathResource(DATA_DIRECTORY + file.getName()));
        }
    }

    @PostConstruct
    public void createTable() {
        if (createTable) {
            System.err.println("Executing scripts...");
            DatabaseConnector.executeDatabaseScript(
                    (connection) -> dbScripts.forEach(resource -> ScriptUtils.executeSqlScript(connection, resource)));
        }
    }

}
