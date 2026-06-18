package com.example.vtaxer.service;

import com.example.vtaxer.model.TaxDetails;
import com.example.vtaxer.repository.TaxDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaxDetailsService {

    @Autowired
    private TaxDetailsRepository taxDetailsRepository;

    public TaxDetails saveOrUpdateTaxDetails(TaxDetails taxDetails) {
        Optional<TaxDetails> existingDetails = taxDetailsRepository.findByUserId(taxDetails.getUserId());
        
        if (existingDetails.isPresent()) {
            TaxDetails existing = existingDetails.get();
            
            // Update taxpayer fields
            existing.setFirstName(taxDetails.getFirstName());
            existing.setMiddleName(taxDetails.getMiddleName());
            existing.setLastName(taxDetails.getLastName());
            existing.setEmail(taxDetails.getEmail());
            existing.setPhone(taxDetails.getPhone());
            existing.setMaritalStatus(taxDetails.getMaritalStatus());
            existing.setSsn(taxDetails.getSsn());
            existing.setDateOfBirth(taxDetails.getDateOfBirth());
            existing.setVisaCategory(taxDetails.getVisaCategory());
            existing.setOccupation(taxDetails.getOccupation());
            existing.setCurrentAddress(taxDetails.getCurrentAddress());
            existing.setCurrentCity(taxDetails.getCurrentCity());
            existing.setCurrentState(taxDetails.getCurrentState());
            existing.setZipCode(taxDetails.getZipCode());
            
            // Handle spouse fields based on marital status
            if ("Single".equals(taxDetails.getMaritalStatus())) {
                // Clear all spouse fields if marital status is Single
                existing.setSpouseFirstName(null);
                existing.setSpouseMiddleName(null);
                existing.setSpouseLastName(null);
                existing.setSpouseEmail(null);
                existing.setSpousePhone(null);
                existing.setSpouseSsn(null);
                existing.setSpouseDateOfBirth(null);
                existing.setSpouseVisaCategory(null);
                existing.setSpouseOccupation(null);
                existing.setSpouseCurrentAddress(null);
                existing.setSpouseCurrentCity(null);
                existing.setSpouseCurrentState(null);
                existing.setSpouseZipCode(null);
                existing.setDateOfMarriage(null);
            } else {
                // Update spouse fields if marital status is Married
                existing.setSpouseFirstName(taxDetails.getSpouseFirstName());
                existing.setSpouseMiddleName(taxDetails.getSpouseMiddleName());
                existing.setSpouseLastName(taxDetails.getSpouseLastName());
                existing.setSpouseEmail(taxDetails.getSpouseEmail());
                existing.setSpousePhone(taxDetails.getSpousePhone());
                existing.setSpouseSsn(taxDetails.getSpouseSsn());
                existing.setSpouseDateOfBirth(taxDetails.getSpouseDateOfBirth());
                existing.setSpouseVisaCategory(taxDetails.getSpouseVisaCategory());
                existing.setSpouseOccupation(taxDetails.getSpouseOccupation());
                existing.setSpouseCurrentAddress(taxDetails.getSpouseCurrentAddress());
                existing.setSpouseCurrentCity(taxDetails.getSpouseCurrentCity());
                existing.setSpouseCurrentState(taxDetails.getSpouseCurrentState());
                existing.setSpouseZipCode(taxDetails.getSpouseZipCode());
                existing.setDateOfMarriage(taxDetails.getDateOfMarriage());
            }
            
            return taxDetailsRepository.save(existing);
        } else {
            // For new records, clear spouse fields if marital status is Single
            if ("Single".equals(taxDetails.getMaritalStatus())) {
                taxDetails.setSpouseFirstName(null);
                taxDetails.setSpouseMiddleName(null);
                taxDetails.setSpouseLastName(null);
                taxDetails.setSpouseEmail(null);
                taxDetails.setSpousePhone(null);
                taxDetails.setSpouseSsn(null);
                taxDetails.setSpouseDateOfBirth(null);
                taxDetails.setSpouseVisaCategory(null);
                taxDetails.setSpouseOccupation(null);
                taxDetails.setSpouseCurrentAddress(null);
                taxDetails.setSpouseCurrentCity(null);
                taxDetails.setSpouseCurrentState(null);
                taxDetails.setSpouseZipCode(null);
                taxDetails.setDateOfMarriage(null);
            }
            return taxDetailsRepository.save(taxDetails);
        }
    }
public Optional<TaxDetails> getByUserId(String userId) {
    return taxDetailsRepository.findByUserId(userId);
}

    public TaxDetails getTaxDetailsByUserId(String userId) {
        return taxDetailsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Tax details not found for user id: " + userId));
    }
}