package com.soen343.project.endpoint.controller;

import com.soen343.project.service.registry.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserRegistryController {

    private final UserRegistry userRegistry;

    @Autowired
    public UserRegistryController(UserRegistry userRegistry) {
        this.userRegistry = userRegistry;
    }

    /**
     * Use case: View active registry
     */
    @GetMapping("/admin/viewActiveUsers")
    public ResponseEntity<?> viewActiveUserRegistry() {
        return new ResponseEntity<>(userRegistry.viewActiveUserRegistry(), HttpStatus.OK);
    }
}
