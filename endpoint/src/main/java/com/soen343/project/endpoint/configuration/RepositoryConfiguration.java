package com.soen343.project.endpoint.configuration;

import com.soen343.project.repository.dao.user.AdminRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Kevin Tan 2018-09-23
 * Configuration class to provide repository beans to project
 */

@Configuration
public class RepositoryConfiguration {

    /**
     * AdminRepository DAO bean
     */
    @Bean
    public AdminRepository adminRepository() {
        return new AdminRepository();
    }

    /**
     * ClientRepository DAO bean
     */
    @Bean
    public ClientRepository clientRepository() {
        return new ClientRepository();
    }
    
}
