package com.example.vtaxer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "taxdetails")
public class TaxDetails {
    @Id
    private String id;
    private String userId;

    // Taxpayer Information
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String maritalStatus;
    private String ssn;
    private String dateOfBirth;
    private String visaCategory;
    private String occupation;
    private String currentAddress;
    private String currentCity;
    private String currentState;
    private String zipCode;

    // Spouse Information
    private String spouseFirstName;
    private String spouseMiddleName;
    private String spouseLastName;
    private String spouseEmail;
    private String spousePhone;
    private String spouseSsn;
    private String spouseDateOfBirth;
    private String spouseVisaCategory;
    private String spouseOccupation;
    private String spouseCurrentAddress;
    private String spouseCurrentCity;
    private String spouseCurrentState;
    private String spouseZipCode;
    private String dateOfMarriage;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Taxpayer getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getVisaCategory() {
        return visaCategory;
    }

    public void setVisaCategory(String visaCategory) {
        this.visaCategory = visaCategory;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    // Spouse getters and setters
    public String getSpouseFirstName() {
        return spouseFirstName;
    }

    public void setSpouseFirstName(String spouseFirstName) {
        this.spouseFirstName = spouseFirstName;
    }

    public String getSpouseMiddleName() {
        return spouseMiddleName;
    }

    public void setSpouseMiddleName(String spouseMiddleName) {
        this.spouseMiddleName = spouseMiddleName;
    }

    public String getSpouseLastName() {
        return spouseLastName;
    }

    public void setSpouseLastName(String spouseLastName) {
        this.spouseLastName = spouseLastName;
    }

    public String getSpouseEmail() {
        return spouseEmail;
    }

    public void setSpouseEmail(String spouseEmail) {
        this.spouseEmail = spouseEmail;
    }

    public String getSpousePhone() {
        return spousePhone;
    }

    public void setSpousePhone(String spousePhone) {
        this.spousePhone = spousePhone;
    }

    public String getSpouseSsn() {
        return spouseSsn;
    }

    public void setSpouseSsn(String spouseSsn) {
        this.spouseSsn = spouseSsn;
    }

    public String getSpouseDateOfBirth() {
        return spouseDateOfBirth;
    }

    public void setSpouseDateOfBirth(String spouseDateOfBirth) {
        this.spouseDateOfBirth = spouseDateOfBirth;
    }

    public String getSpouseVisaCategory() {
        return spouseVisaCategory;
    }

    public void setSpouseVisaCategory(String spouseVisaCategory) {
        this.spouseVisaCategory = spouseVisaCategory;
    }

    public String getSpouseOccupation() {
        return spouseOccupation;
    }

    public void setSpouseOccupation(String spouseOccupation) {
        this.spouseOccupation = spouseOccupation;
    }

    public String getSpouseCurrentAddress() {
        return spouseCurrentAddress;
    }

    public void setSpouseCurrentAddress(String spouseCurrentAddress) {
        this.spouseCurrentAddress = spouseCurrentAddress;
    }

    public String getSpouseCurrentCity() {
        return spouseCurrentCity;
    }

    public void setSpouseCurrentCity(String spouseCurrentCity) {
        this.spouseCurrentCity = spouseCurrentCity;
    }

    public String getSpouseCurrentState() {
        return spouseCurrentState;
    }

    public void setSpouseCurrentState(String spouseCurrentState) {
        this.spouseCurrentState = spouseCurrentState;
    }

    public String getSpouseZipCode() {
        return spouseZipCode;
    }

    public void setSpouseZipCode(String spouseZipCode) {
        this.spouseZipCode = spouseZipCode;
    }

    public String getDateOfMarriage() {
        return dateOfMarriage;
    }

    public void setDateOfMarriage(String dateOfMarriage) {
        this.dateOfMarriage = dateOfMarriage;
    }
}