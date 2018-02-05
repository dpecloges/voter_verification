package com.ots.dpel.android.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VoterDto {

    private Long id;

    private Long electorId;

    private Long electionProcedureId;

    private String round;

    private Long electionDepartmentId;

    private String eklSpecialNo;

    private String lastName;

    private String firstName;

    private String fatherFirstName;

    private String motherFirstName;

    private Date birthDate;

    private Integer birthYear;

    private String address;

    private String addressNo;

    private String city;

    private String postalCode;

    private String country;

    private String cellphone;

    private String email;

    private Boolean voted;

    private Date voteDateTime;

    private Boolean member;

    private Double payment;

    private String idType;

    private String idNumber;

    private String electionDepartmentName;

    private Integer verificationNumber;

    private Boolean voterFirstRound;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getElectorId() {
        return electorId;
    }

    public void setElectorId(Long electorId) {
        this.electorId = electorId;
    }

    public Long getElectionProcedureId() {
        return electionProcedureId;
    }

    public void setElectionProcedureId(Long electionProcedureId) {
        this.electionProcedureId = electionProcedureId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public Long getElectionDepartmentId() {
        return electionDepartmentId;
    }

    public void setElectionDepartmentId(Long electionDepartmentId) {
        this.electionDepartmentId = electionDepartmentId;
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

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public Date getVoteDateTime() {
        return voteDateTime;
    }

    public void setVoteDateTime(Date voteDateTime) {
        this.voteDateTime = voteDateTime;
    }

    public Boolean getMember() {
        return member;
    }

    public void setMember(Boolean member) {
        this.member = member;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public String getElectionDepartmentName() {
        return electionDepartmentName;
    }

    public void setElectionDepartmentName(String electionDepartmentName) {
        this.electionDepartmentName = electionDepartmentName;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Integer getVerificationNumber() {
        return verificationNumber;
    }

    public void setVerificationNumber(Integer verificationNumber) {
        this.verificationNumber = verificationNumber;
    }

    public Boolean getVoterFirstRound() {
        return voterFirstRound;
    }

    public void setVoterFirstRound(Boolean voterFirstRound) {
        this.voterFirstRound = voterFirstRound;
    }

    @Override
    public String toString() {
        return "VoterDto{" +
                "id=" + id +
                ", electorId=" + electorId +
                ", electionProcedureId=" + electionProcedureId +
                ", round='" + round + '\'' +
                ", electionDepartmentId=" + electionDepartmentId +
                ", eklSpecialNo='" + eklSpecialNo + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", fatherFirstName='" + fatherFirstName + '\'' +
                ", motherFirstName='" + motherFirstName + '\'' +
                ", birthDate=" + birthDate +
                ", birthYear=" + birthYear +
                ", address='" + address + '\'' +
                ", addressNo='" + addressNo + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", email='" + email + '\'' +
                ", voted=" + voted +
                ", voteDateTime=" + voteDateTime +
                ", member=" + member +
                ", payment=" + payment +
                ", idType='" + idType + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", electionDepartmentName='" + electionDepartmentName + '\'' +
                ", verificationNumber=" + verificationNumber +
                ", voterFirstRound=" + voterFirstRound +
                '}';
    }
}
