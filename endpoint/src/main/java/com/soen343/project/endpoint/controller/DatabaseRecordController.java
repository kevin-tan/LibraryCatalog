package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.Client;
import com.soen343.project.repository.entity.user.types.UserType;
import com.soen343.project.service.database.RecordDatabase;
import com.soen343.project.repository.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by Kevin Tan 2018-09-25
 */

@Controller
public class DatabaseRecordController {

    private final RecordDatabase recordDatabase;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseRecordController(RecordDatabase recordDatabase, UserRepository userRepository) {
        this.recordDatabase = recordDatabase;
        this.userRepository = userRepository;
    }

    /**
     * Use case: View active registry
     */
    //TODO: This returns all users. Login is needed to view active users.
    @GetMapping("/admin/{id}")
    public ResponseEntity<?> viewActiveUserRegistry(@PathVariable Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (user.getUserType().equals(UserType.ADMIN)) {
            return new ResponseEntity<>(recordDatabase.viewActiveUserRegistry(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Use case: Registration
     */
    //Client Registration
    @PostMapping("/admin/{id}")
    public ResponseEntity<?> registerClient(@PathVariable Long id, @RequestBody Client client) {
        User admin = userRepository.findById(id);
        if (admin == null || client == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (admin.getUserType().equals(UserType.ADMIN)) {
            List<User> usersList = recordDatabase.register(client);
            if(usersList == null){
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            return new ResponseEntity<>(usersList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

}
