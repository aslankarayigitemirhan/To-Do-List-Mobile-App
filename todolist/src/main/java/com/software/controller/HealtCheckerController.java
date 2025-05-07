package com.software.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("")
public class HealtCheckerController {

    @GetMapping("/healthchecker")
    public ResponseEntity<?> checkAWS(){
        return ResponseEntity.status(200).body(null);
    }
}
