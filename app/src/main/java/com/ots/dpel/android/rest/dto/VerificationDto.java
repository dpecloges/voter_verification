package com.ots.dpel.android.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;


@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationDto {

    private Long electorId;

    private String eklSpecialNo;

    private String lastName;

    private String firstName;

    private String fatherFirstName;

    private String motherFirstName;

    private Date birthDate;

    private String municipalRecordNo;

    private String municipalUnitDescription;

    private Boolean hasPreregistrationRecord = false;

    private Boolean preregistrationMember = false;

    private Boolean hasVoterRecord = false;

    private Long voterId;

    private Date voteDateTime;

    private String voteElectionDepartmentName;

    private Double defaultPayment;

    private String voterAddress;

    private String voterAddressNo;

    private String voterCity;

    private String voterPostalCode;

    private String voterCountry;

    private String voterCellphone;

    private String voterEmail;

    private Boolean voterMember;

    private Double voterPayment;

    private Long voterElectionDepartmentId;

    private String voterIdType;

    private String voterIdNumber;

    private Integer voterVerificationNumber;

    private Boolean verifyAnyElector;

    private Boolean voterFirstRound;

    public Long getElectorId() {
        return electorId;
    }

    public void setElectorId(Long electorId) {
        this.electorId = electorId;
    }

    public String getEklSpecialNo() {
        return eklSpecialNo;
    }

    public void setEklSpecialNo(String eklSpecialNo) {
        this.eklSpecialNo = eklSpecialNo;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFatherFirstName() {
        return fatherFirstName;
    }

    public void setFatherFirstName(String fatherFirstName) {
        this.fatherFirstName = fatherFirstName;
    }

    public String getMotherFirstName() {
        return motherFirstName;
    }

    public void setMotherFirstName(String motherFirstName) {
        this.motherFirstName = motherFirstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getMunicipalRecordNo() {
        return municipalRecordNo;
    }

    public void setMunicipalRecordNo(String municipalRecordNo) {
        this.municipalRecordNo = municipalRecordNo;
    }

    public String getMunicipalUnitDescription() {
        return municipalUnitDescription;
    }

    public void setMunicipalUnitDescription(String municipalUnitDescription) {
        this.municipalUnitDescription = municipalUnitDescription;
    }

    public Boolean getHasPreregistrationRecord() {
        return hasPreregistrationRecord;
    }

    public void setHasPreregistrationRecord(Boolean hasPreregistrationRecord) {
        this.hasPreregistrationRecord = hasPreregistrationRecord;
    }

    public Boolean getPreregistrationMember() {
        return preregistrationMember;
    }

    public void setPreregistrationMember(Boolean preregistrationMember) {
        this.preregistrationMember = preregistrationMember;
    }

    public Boolean getHasVoterRecord() {
        return hasVoterRecord;
    }

    public void setHasVoterRecord(Boolean hasVoterRecord) {
        this.hasVoterRecord = hasVoterRecord;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voterId) {
        this.voterId = voterId;
    }

    public Date getVoteDateTime() {
        return voteDateTime;
    }

    public void setVoteDateTime(Date voteDateTime) {
        this.voteDateTime = voteDateTime;
    }

    public String getVoteElectionDepartmentName() {
        return voteElectionDepartmentName;
    }

    public void setVoteElectionDepartmentName(String voteElectionDepartmentName) {
        this.voteElectionDepartmentName = voteElectionDepartmentName;
    }

    public Double getDefaultPayment() {
        return defaultPayment;
    }

    public void setDefaultPayment(Double defaultPayment) {
        this.defaultPayment = defaultPayment;
    }

    public String getVoterAddress() {
        return voterAddress;
    }

    public void setVoterAddress(String voterAddress) {
        this.voterAddress = voterAddress;
    }

    public String getVoterAddressNo() {
        return voterAddressNo;
    }

    public void setVoterAddressNo(String voterAddressNo) {
        this.voterAddressNo = voterAddressNo;
    }

    public String getVoterCity() {
        return voterCity;
    }

    public void setVoterCity(String voterCity) {
        this.voterCity = voterCity;
    }

    public String getVoterPostalCode() {
        return voterPostalCode;
    }

    public void setVoterPostalCode(String voterPostalCode) {
        this.voterPostalCode = voterPostalCode;
    }

    public String getVoterCountry() {
        return voterCountry;
    }

    public void setVoterCountry(String voterCountry) {
        this.voterCountry = voterCountry;
    }

    public String getVoterCellphone() {
        return voterCellphone;
    }

    public void setVoterCellphone(String voterCellphone) {
        this.voterCellphone = voterCellphone;
    }

    public String getVoterEmail() {
        return voterEmail;
    }

    public void setVoterEmail(String voterEmail) {
        this.voterEmail = voterEmail;
    }

    public Boolean getVoterMember() {
        return voterMember;
    }

    public void setVoterMember(Boolean voterMember) {
        this.voterMember = voterMember;
    }

    public Double getVoterPayment() {
        return voterPayment;
    }

    public void setVoterPayment(Double voterPayment) {
        this.voterPayment = voterPayment;
    }

    public Long getVoterElectionDepartmentId() {
        return voterElectionDepartmentId;
    }

    public void setVoterElectionDepartmentId(Long voterElectionDepartmentId) {
        this.voterElectionDepartmentId = voterElectionDepartmentId;
    }

    public String getVoterIdType() {
        return voterIdType;
    }

    public void setVoterIdType(String voterIdType) {
        this.voterIdType = voterIdType;
    }

    public String getVoterIdNumber() {
        return voterIdNumber;
    }

    public void setVoterIdNumber(String voterIdNumber) {
        this.voterIdNumber = voterIdNumber;
    }

    public Integer getVoterVerificationNumber() {
        return voterVerificationNumber;
    }

    public void setVoterVerificationNumber(Integer voterVerificationNumber) {
        this.voterVerificationNumber = voterVerificationNumber;
    }

    public Boolean getVerifyAnyElector() {
        return verifyAnyElector;
    }

    public void setVerifyAnyElector(Boolean verifyAnyElector) {
        this.verifyAnyElector = verifyAnyElector;
    }

    public Boolean getVoterFirstRound() {
        return voterFirstRound;
    }

    public void setVoterFirstRound(Boolean voterFirstRound) {
        this.voterFirstRound = voterFirstRound;
    }

    @Override
    public String toString() {
        return "VerificationDto{" +
                "electorId=" + electorId +
                ", eklSpecialNo='" + eklSpecialNo + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", fatherFirstName='" + fatherFirstName + '\'' +
                ", motherFirstName='" + motherFirstName + '\'' +
                ", birthDate=" + birthDate +
                ", municipalRecordNo='" + municipalRecordNo + '\'' +
                ", municipalUnitDescription='" + municipalUnitDescription + '\'' +
                ", hasPreregistrationRecord=" + hasPreregistrationRecord +
                ", preregistrationMember=" + preregistrationMember +
                ", hasVoterRecord=" + hasVoterRecord +
                ", voterId=" + voterId +
                ", voteDateTime=" + voteDateTime +
                ", voteElectionDepartmentName='" + voteElectionDepartmentName + '\'' +
                ", defaultPayment=" + defaultPayment +
                ", voterAddress='" + voterAddress + '\'' +
                ", voterAddressNo='" + voterAddressNo + '\'' +
                ", voterCity='" + voterCity + '\'' +
                ", voterPostalCode='" + voterPostalCode + '\'' +
                ", voterCountry='" + voterCountry + '\'' +
                ", voterCellphone='" + voterCellphone + '\'' +
                ", voterEmail='" + voterEmail + '\'' +
                ", voterMember=" + voterMember +
                ", voterPayment=" + voterPayment +
                ", voterElectionDepartmentId=" + voterElectionDepartmentId +
                ", voterIdType='" + voterIdType + '\'' +
                ", voterIdNumber='" + voterIdNumber + '\'' +
                ", voterVerificationNumber=" + voterVerificationNumber +
                ", verifyAnyElector=" + verifyAnyElector +
                ", voterFirstRound=" + voterFirstRound +
                '}';
    }
}
