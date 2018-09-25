package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by Kevin Tan 2018-09-25
 */

@Controller
public class DatabaseRecordController {

    private final UserRepository userRepository;

    public DatabaseRecordController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Use case: View active registry
     */
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> viewActiveUserRegistry(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Use case: Registration
     */
    @PostMapping("/admin/{id}")
    public ResponseEntity<?> registerUser(@PathVariable Long id, @RequestBody User user) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
