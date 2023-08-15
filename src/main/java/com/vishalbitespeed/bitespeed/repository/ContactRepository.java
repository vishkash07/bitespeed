package com.vishalbitespeed.bitespeed.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.vishalbitespeed.bitespeed.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Custom query methods if needed
    @Query(value = "SELECT * FROM vishalbitespeed.contact " +
    "WHERE (vishalbitespeed.contact.email = :email OR vishalbitespeed.contact.phone_number = :phoneNumber ) " +
    "OR (vishalbitespeed.contact.phone_number = :phoneNumber AND vishalbitespeed.contact.email IS NULL) " +
    "OR (vishalbitespeed.contact.phone_number IN ( " +
    "    SELECT phone_number " +
    "    FROM vishalbitespeed.contact " +
    "    WHERE email = :email " +
    ")) OR (vishalbitespeed.contact.email IN ( " +
    "    SELECT email " +
    "    FROM vishalbitespeed.contact " +
    "    WHERE phone_number = :phoneNumber " +
    "    ) AND  vishalbitespeed.contact.email IS NOT NULL AND vishalbitespeed.contact.email <> :email) " +
    "OR (vishalbitespeed.contact.phone_number IN ( " +
    "    SELECT phone_number " +
    "    FROM vishalbitespeed.contact " +
    "    WHERE email = :email " +
    "    ) AND  vishalbitespeed.contact.email IS NOT NULL AND vishalbitespeed.contact.phone_number <> :phoneNumber)",
nativeQuery = true)
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);

    List<Contact>  findByPhoneNumberAndLinkPrecedence(String email,String linkPrecedence);
    List<Contact>  findByEmailAndLinkPrecedence(String email,String linkPrecedence);
    Contact findByEmailAndPhoneNumber(String email, String phoneNumber);
    

}