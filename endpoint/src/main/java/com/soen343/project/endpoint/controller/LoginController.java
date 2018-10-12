package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.service.authenticate.LoginManager;
import com.soen343.project.service.registry.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoginController {

    private final LoginManager loginManager;
    private final UserRegistry userRegistry;
    private final UserRepository userRepository;

    @Autowired
    public LoginController(LoginManager loginManager, UserRegistry userRegistry, UserRepository userRepository) {
        this.loginManager = loginManager;
        this.userRegistry = userRegistry;
        this.userRepository = userRepository;
    }

    //TODO: should pass username and not id for login
    @GetMapping("/login/{id}")
    public ResponseEntity<?> login(@PathVariable Long id) {
        loginManager.loginUser(userRepository.findById(id));
        return new ResponseEntity<>(userRegistry.viewActiveUserRegistry(1L), HttpStatus.OK);
    }

    //TODO: should pass username and not id for logout
    @GetMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable Long id) {
        loginManager.logoutUser(userRepository.findById(id));
        return new ResponseEntity<>(userRegistry.viewActiveUserRegistry(1L), HttpStatus.OK);
    }
}
