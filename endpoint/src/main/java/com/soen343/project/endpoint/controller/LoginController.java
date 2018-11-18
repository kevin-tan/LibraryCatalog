package com.soen343.project.endpoint.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soen343.project.repository.dao.user.UserGateway;
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
    private final UserGateway userGateway;

    @Autowired
    public LoginController(LoginManager loginManager, UserGateway userGateway){
        this.loginManager = loginManager;
        this.userGateway= userGateway;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody ObjectNode email) {
        return new ResponseEntity<>(loginManager.loginUser(email.get("email").asText()), HttpStatus.OK);
    }
}
