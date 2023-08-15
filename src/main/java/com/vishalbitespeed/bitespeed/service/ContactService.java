package com.vishalbitespeed.bitespeed.service;

import com.vishalbitespeed.bitespeed.dto.IdentificationRequest;
import com.vishalbitespeed.bitespeed.dto.IdentificationResponse;

public interface ContactService {
    IdentificationResponse identifyContact(IdentificationRequest request);
}
