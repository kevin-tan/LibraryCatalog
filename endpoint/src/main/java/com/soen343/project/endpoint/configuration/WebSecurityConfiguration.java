package com.soen343.project.endpoint.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import javax.sql.DataSource;


@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .antMatchers("/admin/**").access("hasRole('ADMIN')")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
//                    .loginPage("/login")
                    .defaultSuccessUrl("/notfound")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/index")

                 ;
    }



    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception{
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("SELECT firstName as username, lastName as password, true, id as id from User where firstName=?").authoritiesByUsernameQuery("SELECT firstName as username, userType as role, id as id from User where firstName=?");

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

}




