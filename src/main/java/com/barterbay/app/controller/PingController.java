package com.barterbay.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {

    @GetMapping
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Works fine");
    }

    @GetMapping("/a")
    public ResponseEntity<String> ping2() {
        return ResponseEntity.ok("Works fine!!@");
    }

}
