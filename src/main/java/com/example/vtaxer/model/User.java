package com.example.vtaxer.model;
 
import java.time.LocalDateTime;
 
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonIgnore;
 
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
    private String alternatePhone;
    private String maritalStatus;
    private String ssn;
    private String dateOfBirth;
    private String visaCategory;
    private String occupation;
    private String currentAddress;
    private String currentCity;
    private String currentState;
    private String zipCode;
    private String status;
    private String state;
private String lastTaxReturnYear;
 
@CreatedDate
private LocalDateTime createdAt;
 
private LocalDateTime lastLoginAt;
    @JsonIgnore
    private String password;
 
    // Getters and Setters
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
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
 
    public String getAlternatePhone() {
        return alternatePhone;
    }
 
    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }
 
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
 
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
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
    return status;
}
 
public void setStatus(String status) {
    this.status = status;
}
 
public String getLastTaxReturnYear() {
    return lastTaxReturnYear;
}
 
public void setLastTaxReturnYear(String lastTaxReturnYear) {
    this.lastTaxReturnYear = lastTaxReturnYear;
}
 
public LocalDateTime getCreatedAt() {
    return createdAt;
}
 
public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}
 
public LocalDateTime getLastLoginAt() {
    return lastLoginAt;
}
 
public void setLastLoginAt(LocalDateTime lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
}
}