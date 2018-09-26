package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.User;
import com.soen343.project.repository.entity.user.types.UserType;
import com.soen343.project.service.database.RecordDatabase;
import com.soen343.project.service.database.UserRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserRegistryController {

    private final UserRegistry userRegistry;
    private final UserRepository userRepository;

    @Autowired
    public UserRegistryController(UserRegistry userRegistry, UserRepository userRepository) {
        this.userRegistry = userRegistry;
        this.userRepository = userRepository;
    }

    /**
     * Use case: View active registry
     */
    //TODO: This returns all users. Login is needed to view active users.
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> viewActiveUserRegistry(@PathVariable Long id) {
        return new ResponseEntity<>(userRegistry.viewActiveUserRegistry(id), HttpStatus.OK);
    }
}