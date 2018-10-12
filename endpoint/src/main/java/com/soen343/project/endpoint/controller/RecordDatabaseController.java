package com.soen343.project.endpoint.controller;


import com.soen343.project.service.database.RecordDatabase;
import com.soen343.project.repository.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by Kevin Tan 2018-09-25
 */

@Controller
public class RecordDatabaseController {

    private final RecordDatabase recordDatabase;

    @Autowired
    public RecordDatabaseController(RecordDatabase recordDatabase) {
        this.recordDatabase = recordDatabase;
    }

    /**
     * Use case: Registration
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return new ResponseEntity<>(recordDatabase.register(user), HttpStatus.OK);
    }

}
