package com.soen343.project.endpoint.controller;

import com.soen343.project.repository.dao.user.UserRepository;
import com.soen343.project.repository.entity.user.Admin;
import com.soen343.project.repository.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/test/getUser")
    public ResponseEntity<?> getUser() {
        return new ResponseEntity<>(userRepository.find(1L), HttpStatus.OK);
    }

    /**
     * Clear DB after this request
     */
    @GetMapping("/test/addNewUser")
    public ResponseEntity<?> addNewUser() {
        Admin admin = Admin.builder().firstName("Test First").lastName("Test Last").email("Test@hotmail.com").phoneNumber("514-Test")
                .physicalAddress("888 Test").build();
        userRepository.save(admin);
        return new ResponseEntity<>(userRepository.find(2L), HttpStatus.OK);
    }

    /**
     * Clear DB after this request
     */
    @GetMapping("/test/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id) {
        User oldAdmin = userRepository.find(id);
        Admin admin = Admin.builder().firstName("Test First2").lastName("Test Last2").email("Tes2t@hotmail.com32").phoneNumber("51324-Tes32t")
                .physicalAddress("88832 Test32").id(oldAdmin.getId()).build();
        userRepository.update(admin);
        return new ResponseEntity<>(userRepository.find(oldAdmin.getId()), HttpStatus.OK);
    }

    /**
     * Clear DB after this request
     */
    @GetMapping("/test/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User admin = userRepository.find(id);
        userRepository.delete(admin);
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }
}
