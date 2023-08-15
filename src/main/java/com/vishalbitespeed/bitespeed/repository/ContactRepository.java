package com.vishalbitespeed.bitespeed.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.vishalbitespeed.bitespeed.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Custom query methods if needed
    @Query(value = "SELECT * FROM railway.contact " +
    "WHERE (railway.contact.email = :email OR railway.contact.phone_number = :phoneNumber ) " +
    "OR (railway.contact.phone_number = :phoneNumber AND railway.contact.email IS NULL) " +
    "OR (railway.contact.phone_number IN ( " +
    "    SELECT phone_number " +
    "    FROM railway.contact " +
    "    WHERE email = :email " +
    ")) OR (railway.contact.email IN ( " +
    "    SELECT email " +
    "    FROM railway.contact " +
    "    WHERE phone_number = :phoneNumber " +
    "    ) AND  railway.contact.email IS NOT NULL AND railway.contact.email <> :email) " +
    "OR (railway.contact.phone_number IN ( " +
    "    SELECT phone_number " +
    "    FROM railway.contact " +
    "    WHERE email = :email " +
    "    ) AND  railway.contact.email IS NOT NULL AND railway.contact.phone_number <> :phoneNumber)",
nativeQuery = true)
    List<Contact> findByEmailOrPhoneNumber(String email, String phoneNumber);

    List<Contact>  findByPhoneNumberAndLinkPrecedence(String email,String linkPrecedence);
    List<Contact>  findByEmailAndLinkPrecedence(String email,String linkPrecedence);
    Contact findByEmailAndPhoneNumber(String email, String phoneNumber);
    

}