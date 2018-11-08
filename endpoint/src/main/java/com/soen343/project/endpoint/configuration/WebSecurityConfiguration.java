package com.soen343.project.endpoint.configuration;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soen343.project.endpoint.configuration.security.BasicAuthEntryPoint;
import com.soen343.project.endpoint.configuration.security.MySavedRequestAwareAuthenticationSuccessHandler;
import com.soen343.project.service.authenticate.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.stream.Collectors;

@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final BasicAuthEntryPoint basicAuthEntryPoint;
    private final SimpleUrlAuthenticationFailureHandler myFailureHandler;
    private final MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler;
    private final LoginManager loginManager;

    @Autowired
    public WebSecurityConfiguration(BasicAuthEntryPoint basicAuthEntryPoint,
                                    SimpleUrlAuthenticationFailureHandler myFailureHandler,
                                    MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler,
                                    LoginManager loginManager) {
        this.basicAuthEntryPoint = basicAuthEntryPoint;
        this.myFailureHandler = myFailureHandler;
        this.mySuccessHandler = mySuccessHandler;
        this.loginManager = loginManager;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().exceptionHandling()
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasAuthority("Admin")
                .antMatchers("/user/**").hasAnyAuthority("Admin", "Client")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .successHandler(mySuccessHandler)
                .failureHandler(myFailureHandler)
                .and()
                .logout()
                .addLogoutHandler(getLogoutHandler())
                .and()
                .httpBasic()
                .authenticationEntryPoint(basicAuthEntryPoint);
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT email as username, password, true from User where email=?")
                .authoritiesByUsernameQuery("SELECT email as username, userType as role from User where email=?");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type");
    }

    private LogoutHandler getLogoutHandler(){
        return (request, response, authentication) -> {
            try {
                String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonBody = mapper.readTree(body);
                String email = jsonBody.get("email").asText();
                loginManager.logoutUser(email);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("");
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

}




