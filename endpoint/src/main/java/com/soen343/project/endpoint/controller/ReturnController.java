package com.soen343.project.endpoint.controller;

import com.soen343.project.service.handler.CartHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ReturnController {

    private final CartHandler cartHandler;

    @Autowired
    public ReturnController(CartHandler cartHandler) {
        this.cartHandler = cartHandler;
    }

    @PostMapping("/{clientId}/return")
    public ResponseEntity<?> returnItems(@PathVariable Long clientId) {
        //return new ResponseEntity<>(cartHandler.clear(clientId), HttpStatus.OK);
    }
}
