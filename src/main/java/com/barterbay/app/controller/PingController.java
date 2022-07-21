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
    System.out.println();

    Integer a = null;
    a.byteValue();
    //return ResponseEntity.ok("Works fine");
  }

}
