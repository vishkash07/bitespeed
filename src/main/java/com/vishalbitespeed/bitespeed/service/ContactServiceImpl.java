package com.vishalbitespeed.bitespeed.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vishalbitespeed.bitespeed.dto.IdentificationRequest;
import com.vishalbitespeed.bitespeed.dto.IdentificationResponse;
import com.vishalbitespeed.bitespeed.entity.Contact;
import com.vishalbitespeed.bitespeed.repository.ContactRepository;


@Service
public class ContactServiceImpl implements ContactService  {
    
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
    
    private IdentificationResponse processContacts(String email, String phoneNumber, List<Contact> contacts, int p) {
        if (p == 1) {
            
            Contact prevContact = contactRepository.findByEmailAndPhoneNumber(email, phoneNumber);
            if (prevContact != null){
                updateExistingContact(prevContact);
            }
            else{Contact newContact = new Contact();
            if (email != null) {
                newContact.setEmail(email);
            }

            if (phoneNumber != null) {
                newContact.setPhoneNumber(phoneNumber);
            }
            newContact.setLinkPrecedence("secondary");
            newContact.setCreatedAt(LocalDateTime.now());
            newContact.setUpdatedAt(LocalDateTime.now());
            contactRepository.save(newContact);}

        }
        List<Contact> newAllcontacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);
        Contact primaryContact = contacts.stream()
                .filter(contact -> "primary".equals(contact.getLinkPrecedence()))
                .findFirst()
                .orElse(null);

        List<Contact> primaryContacts = contacts.stream()
                .filter(contact -> "primary".equals(contact.getLinkPrecedence()))
                .collect(Collectors.toList());

        if (primaryContacts.size() > 1) {
            System.out.println(primaryContacts.get(1).getEmail());;
            Contact secondPrimaryContact = primaryContacts.get(1);
            // secondPrimaryContact.setEmail(secondPrimaryContact.getEmail().equals(email) ? secondPrimaryContact.getEmail() : email);
            // secondPrimaryContact.setPhoneNumber(secondPrimaryContact.getPhoneNumber().equals(phoneNumber) ? secondPrimaryContact.getPhoneNumber() : phoneNumber);
            secondPrimaryContact.setLinkPrecedence("secondary");
            secondPrimaryContact.setUpdatedAt(LocalDateTime.now());
            secondPrimaryContact.setLinkedId(primaryContacts.get(0).getId());
            contactRepository.save(secondPrimaryContact);

            newAllcontacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);
            List<Long> secondaryContactIds = new ArrayList<>();
            List<String> emails = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();

            // Iterate through other contacts to update their data
            for (Contact contact : newAllcontacts) {
                if ("secondary".equals(contact.getLinkPrecedence())) {
                    if (!secondaryContactIds.contains(contact.getId())) {
                        secondaryContactIds.add(contact.getId());
                    }}
                    if (!phoneNumbers.contains(contact.getPhoneNumber()) && !Objects.isNull(contact.getPhoneNumber())) {
                    phoneNumbers.add(contact.getPhoneNumber());
                }
                if (!Objects.isNull(contact.getEmail()) && !emails.contains(contact.getEmail())) {
                    emails.add(contact.getEmail());
                }
                
            }

            // Create the response 
            return createResponse(primaryContact.getId(), emails, phoneNumbers, secondaryContactIds);

        }

        // Iterate through other contacts to update their data
        if (primaryContacts.get(0) != null && primaryContacts.size() == 1) {
            List<Long> secondaryContactIds = new ArrayList<>();
            List<String> emails = new ArrayList<>();
            List<String> phoneNumbers = new ArrayList<>();
            Long linkId = primaryContacts.get(0).getId();
            
            for (Contact contact : newAllcontacts) {
                if ("secondary".equals(contact.getLinkPrecedence())) {
                    if (!secondaryContactIds.contains(contact.getId())) {
                        secondaryContactIds.add(contact.getId());
                    }
                    contact.setLinkedId(linkId);
                    contactRepository.save(contact);

                }
                if (!phoneNumbers.contains(contact.getPhoneNumber()) && !Objects.isNull(contact.getPhoneNumber())) {
                    phoneNumbers.add(contact.getPhoneNumber());
                }
                if (!Objects.isNull(contact.getEmail()) && !emails.contains(contact.getEmail())) {
                    emails.add(contact.getEmail());
                }
            }

            return createResponse(primaryContact.getId(), emails, phoneNumbers, secondaryContactIds);

        }
        return null;
    }


    //For Updating the contact if it already exsists
    private void updateExistingContact(Contact existingContact) {
        existingContact.setUpdatedAt(LocalDateTime.now());
        contactRepository.save(existingContact);
    }

    //first time creating a contact
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

    //response generator
    private IdentificationResponse createResponse(Long primaryContactId, List<String> emails, List<String> phoneNumbers,
            List<Long> secondaryContactIds) {
        return new IdentificationResponse(
                primaryContactId,
                emails,
                phoneNumbers,
                secondaryContactIds);
    }
}
