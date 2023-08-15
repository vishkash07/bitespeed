package com.vishalbitespeed.bitespeed.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    private IdentificationResponse createNewContact(String email, String phoneNumber) {
        Contact newContact = new Contact();
        newContact.setEmail(email);
        newContact.setPhoneNumber(phoneNumber);
        newContact.setLinkPrecedence("primary");
        newContact.setCreatedAt(LocalDateTime.now());
        newContact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(newContact);

        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        List<Long> secondaryContactIds = new ArrayList<>();

        emails.add(newContact.getEmail());
        phoneNumbers.add(newContact.getPhoneNumber());

        return createResponse(newContact.getId(), emails, phoneNumbers, secondaryContactIds);
    }

    private IdentificationResponse createResponse(Long primaryContactId, List<String> emails, List<String> phoneNumbers,
            List<Long> secondaryContactIds) {
        return new IdentificationResponse(
                primaryContactId,
                emails,
                phoneNumbers,
                secondaryContactIds);
    }
}
