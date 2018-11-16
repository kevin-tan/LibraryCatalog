package com.soen343.project.endpoint.controller;


import com.soen343.project.repository.entity.user.User;
import com.soen343.project.service.database.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Kevin Tan 2018-09-25
 */

@Controller
@RequestMapping("/admin")
public class LibraryController {

    private final Library library;

    @Autowired
    public LibraryController(Library library) {
        this.library = library;
    }

    /**
     * Use case: Registration
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        return new ResponseEntity<>(library.register(user), HttpStatus.OK);

    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return new ResponseEntity<>(library.getUserByID(id), HttpStatus.OK);
    }
}
