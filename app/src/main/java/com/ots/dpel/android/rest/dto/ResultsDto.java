package com.ots.dpel.android.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultsDto {

    private Long id;

    private String code;

    private String name;

    private Integer totalVotes;

    private Integer whiteVotes;

    private Integer invalidVotes;

    private Integer validVotes;

    private Integer candidateOneVotes;

    private Integer candidateTwoVotes;

    private Integer candidateThreeVotes;

    private Integer candidateFourVotes;

    private Integer candidateFiveVotes;

    private Integer candidateSixVotes;

    private Integer candidateSevenVotes;

    private Integer candidateEightVotes;

    private Integer candidateNineVotes;

    private Integer candidateTenVotes;

    private Boolean submitted;

    private String attachmentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(Integer totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Integer getWhiteVotes() {
        return whiteVotes;
    }

    public void setWhiteVotes(Integer whiteVotes) {
        this.whiteVotes = whiteVotes;
    }

    public Integer getInvalidVotes() {
        return invalidVotes;
    }

    public void setInvalidVotes(Integer invalidVotes) {
        this.invalidVotes = invalidVotes;
    }

    public Integer getValidVotes() {
        return validVotes;
    }

    public void setValidVotes(Integer validVotes) {
        this.validVotes = validVotes;
    }

    public Integer getCandidateOneVotes() {
        return candidateOneVotes;
    }

    public void setCandidateOneVotes(Integer candidateOneVotes) {
        this.candidateOneVotes = candidateOneVotes;
    }

    public Integer getCandidateTwoVotes() {
        return candidateTwoVotes;
    }

    public void setCandidateTwoVotes(Integer candidateTwoVotes) {
        this.candidateTwoVotes = candidateTwoVotes;
    }

    public Integer getCandidateThreeVotes() {
        return candidateThreeVotes;
    }

    public void setCandidateThreeVotes(Integer candidateThreeVotes) {
        this.candidateThreeVotes = candidateThreeVotes;
    }

    public Integer getCandidateFourVotes() {
        return candidateFourVotes;
    }

    public void setCandidateFourVotes(Integer candidateFourVotes) {
        this.candidateFourVotes = candidateFourVotes;
    }

    public Integer getCandidateFiveVotes() {
        return candidateFiveVotes;
    }

    public void setCandidateFiveVotes(Integer candidateFiveVotes) {
        this.candidateFiveVotes = candidateFiveVotes;
    }

    public Integer getCandidateSixVotes() {
        return candidateSixVotes;
    }

    public void setCandidateSixVotes(Integer candidateSixVotes) {
        this.candidateSixVotes = candidateSixVotes;
    }

    public Integer getCandidateSevenVotes() {
        return candidateSevenVotes;
    }

    public void setCandidateSevenVotes(Integer candidateSevenVotes) {
        this.candidateSevenVotes = candidateSevenVotes;
    }

    public Integer getCandidateEightVotes() {
        return candidateEightVotes;
    }

    public void setCandidateEightVotes(Integer candidateEightVotes) {
        this.candidateEightVotes = candidateEightVotes;
    }

    public Integer getCandidateNineVotes() {
        return candidateNineVotes;
    }

    public void setCandidateNineVotes(Integer candidateNineVotes) {
        this.candidateNineVotes = candidateNineVotes;
    }

    public Integer getCandidateTenVotes() {
        return candidateTenVotes;
    }

    public void setCandidateTenVotes(Integer candidateTenVotes) {
        this.candidateTenVotes = candidateTenVotes;
    }

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }
}
