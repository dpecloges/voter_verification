package com.ots.dpel.android.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by tasos on 10/10/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Token {

    @JsonProperty("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /*private Long electionCenterId;

    private String electionCenterName;

    private Long electionDepartmentId;

    private String electionDepartmentName;

    private String fullName;

    @JsonProperty("expires_in")
    private Long expiresIn;

    private String jti;

    private String scope;

    @JsonProperty("token_type")
    private String tokenType;

    public Long getElectionCenterId() {
        return electionCenterId;
    }

    public void setElectionCenterId(Long electionCenterId) {
        this.electionCenterId = electionCenterId;
    }

    public String getElectionCenterName() {
        return electionCenterName;
    }

    public void setElectionCenterName(String electionCenterName) {
        this.electionCenterName = electionCenterName;
    }

    public Long getElectionDepartmentId() {
        return electionDepartmentId;
    }

    public void setElectionDepartmentId(Long electionDepartmentId) {
        this.electionDepartmentId = electionDepartmentId;
    }

    public String getElectionDepartmentName() {
        return electionDepartmentName;
    }

    public void setElectionDepartmentName(String electionDepartmentName) {
        this.electionDepartmentName = electionDepartmentName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", electionCenterId=" + electionCenterId +
                ", electionCenterName='" + electionCenterName + '\'' +
                ", electionDepartmentId=" + electionDepartmentId +
                ", electionDepartmentName='" + electionDepartmentName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", expiresIn=" + expiresIn +
                ", jti='" + jti + '\'' +
                ", scope='" + scope + '\'' +
                ", tokenType='" + tokenType + '\'' +
                '}';
    }*/
}
