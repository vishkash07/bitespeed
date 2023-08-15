package com.vishalbitespeed.bitespeed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vishalbitespeed.bitespeed.dto.IdentificationRequest;
import com.vishalbitespeed.bitespeed.dto.IdentificationResponse;
import com.vishalbitespeed.bitespeed.entity.Contact;
import com.vishalbitespeed.bitespeed.repository.ContactRepository;

import lombok.extern.slf4j.Slf4j;

@Service

public class ContactServiceImpl {
    
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public IdentificationResponse identifyContact(IdentificationRequest request) {
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();
        int p = 1;

        List<Contact> contacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        if (contacts.isEmpty()) {
            return createNewContact(email, phoneNumber);
        }
        List<Contact> emailPrimaryContacts = contactRepository.findByEmailAndLinkPrecedence(email, "primary");
        List<Contact> phonePrimaryContacts = contactRepository.findByPhoneNumberAndLinkPrecedence(phoneNumber,
                "primary");

        if (!emailPrimaryContacts.isEmpty() && !phonePrimaryContacts.isEmpty()
                && !emailPrimaryContacts.equals(phonePrimaryContacts)) {
            p = 0;
            
            return processContacts(email, phoneNumber, contacts, p);
        }

        return processContacts(email, phoneNumber, contacts, p);

    }
}
