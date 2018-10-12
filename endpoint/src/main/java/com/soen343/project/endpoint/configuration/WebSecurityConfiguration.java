package com.soen343.project.endpoint.configuration;


import com.soen343.project.endpoint.configuration.security.BasicAuthEntryPoint;
import com.soen343.project.endpoint.configuration.security.MySavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;


@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final BasicAuthEntryPoint basicAuthEntryPoint;
    private final SimpleUrlAuthenticationFailureHandler myFailureHandler;
    private final MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;

    @Autowired
    public WebSecurityConfiguration(BasicAuthEntryPoint basicAuthEntryPoint,
                                    SimpleUrlAuthenticationFailureHandler myFailureHandler,
                                    MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler) {
        this.basicAuthEntryPoint = basicAuthEntryPoint;
        this.myFailureHandler = myFailureHandler;
        this.mySuccessHandler = mySuccessHandler;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("Admin")
                .antMatchers("/admin/**").access("hasRole('Admin')")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)
                .and()
                .logout()
                .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthEntryPoint);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT email, password, true from User where email=?")
                .authoritiesByUsernameQuery("SELECT email, userType as role from User where email=?");
    }

}




