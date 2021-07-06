package com.npd.employees.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {

    @GetMapping(value = "admin/test")
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok("Secured Hello World!");
    }

    @GetMapping(value = "/test")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("Hello World!");
    }

}
