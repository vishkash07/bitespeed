package com.vishalbitespeed.bitespeed.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vishalbitespeed.bitespeed.dto.IdentificationRequest;
import com.vishalbitespeed.bitespeed.dto.IdentificationResponse;
import com.vishalbitespeed.bitespeed.service.ContactService;

@RestController
public class ContactController {

     @Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public ResponseEntity<IdentificationResponse> identifyContact(@RequestBody IdentificationRequest request) {
        IdentificationResponse response = contactService.identifyContact(request);
        if (response != null) {
            
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
}
