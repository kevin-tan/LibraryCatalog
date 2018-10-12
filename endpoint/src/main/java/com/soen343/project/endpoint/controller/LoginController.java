package com.soen343.project.endpoint.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/v1")
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return new ResponseEntity<>("Login successful.", HttpStatus.OK);
    }
}
