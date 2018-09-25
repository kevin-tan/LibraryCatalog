package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Kevin Tan 2018-09-25
 */

@Controller
public class ExampleController {

    private final UserRepository userRepository;

    @Autowired
    public ExampleController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/test")
    public ResponseEntity<?> getAdmin() {
        return new ResponseEntity<>(userRepository.find(1L), HttpStatus.OK);
    }
}
