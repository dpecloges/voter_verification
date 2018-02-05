package com.ots.dpel.android.rest.dto;

import java.util.Date;

public class VerificationArgs {

    private String searchType;

    private String eklSpecialNo;

    private String lastName;

    private String firstName;

    private String fatherFirstName;

    private String motherFirstName;

    private Integer birthYear;

    private Date birthDate;

    private Integer voterVerificationNumber;

    private Long voterElectionDepartmentId;

    private Long voterElectionProcedureId;

    private String voterRound;

    public Long getVoterElectionProcedureId() {
        return voterElectionProcedureId;
    }

    public void setVoterElectionProcedureId(Long voterElectionProcedureId) {
        this.voterElectionProcedureId = voterElectionProcedureId;
    }

    public String getVoterRound() {
        return voterRound;
    }

    public void setVoterRound(String voterRound) {
        this.voterRound = voterRound;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
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

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getVoterVerificationNumber() {
        return voterVerificationNumber;
    }

    public void setVoterVerificationNumber(Integer voterVerificationNumber) {
        this.voterVerificationNumber = voterVerificationNumber;
    }

    public Long getVoterElectionDepartmentId() {
        return voterElectionDepartmentId;
    }

    public void setVoterElectionDepartmentId(Long voterElectionDepartmentId) {
        this.voterElectionDepartmentId = voterElectionDepartmentId;
    }
}
