package com.soen343.project.endpoint.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soen343.project.service.authenticate.LoginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/v1")
public class LoginController {

    private final LoginManager loginManager;

    @Autowired
    public LoginController(LoginManager loginManager){
        this.loginManager = loginManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ObjectNode email) {
        return new ResponseEntity<>(loginManager.loginUser(email.get("email").asText()), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody ObjectNode email) {
        return new ResponseEntity<>(loginManager.logoutUser(email.get("email").asText()), HttpStatus.OK);
    }
}
