package com.soen343.project.endpoint.configuration;

import com.soen343.project.database.schema.CreateDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * Created by Kevin Tan 2018-09-17
 */

@ComponentScan(basePackages = {"com.soen343.project.service", "com.soen343.project.repository", "com.soen343.project.database"})
@Configuration
public class ServerConfiguration {

    /**
     * True, will drop and create all tables in database on startup
     * False, will do nothing on startup
     * Default value is set to True
     */
    @Value("${sqlite.database.dll-auto.create:false}")
    private boolean dropCreateDatabase;

    /**
     * Bean to auto create tables (if table exists will override)
     */
    @Bean
    public CreateDatabase createDatabase() {
        return new CreateDatabase(dropCreateDatabase);
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("org.sqlite.JDBC");
        driverManagerDataSource.setUrl("jdbc:sqlite:data/LibraryCatalog.db");
        return driverManagerDataSource;
    }


}
