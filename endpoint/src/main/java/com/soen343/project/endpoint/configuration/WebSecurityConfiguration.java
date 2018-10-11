package com.soen343.project.endpoint.configuration;


import com.soen343.project.endpoint.configuration.security.BasicAuthEntryPoint;
import com.soen343.project.endpoint.configuration.security.MySavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;


@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final BasicAuthEntryPoint basicAuthEntryPoint;

    @Autowired
    public WebSecurityConfiguration(BasicAuthEntryPoint basicAuthEntryPoint){
        this.basicAuthEntryPoint=basicAuthEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().exceptionHandling()
                    .and()
                .authorizeRequests()
//                    .antMatchers("/admin/**").hasRole("ADMIN")
//                    .antMatchers("/admin/**").access("hasRole('ADMIN')")
                    .anyRequest().authenticated()
                    .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthEntryPoint)
                    .and()
                .formLogin()
                    .successHandler(mySuccessHandler())
                    .failureHandler(myFailureHandler())
                    .and()
                .logout()
                ;
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception{
        auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery("SELECT username, password, true from User where username=?").authoritiesByUsernameQuery("SELECT username, userType as role from User where username=?");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler myFailureHandler(){
        return new SimpleUrlAuthenticationFailureHandler();
    }

    @Bean
    public MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler(){
        return new MySavedRequestAwareAuthenticationSuccessHandler();
    }

}




