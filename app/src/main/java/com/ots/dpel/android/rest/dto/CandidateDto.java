package com.ots.dpel.android.rest.dto;

public class CandidateDto {

    private Long id;

    private String lastName;

    private String firstName;

    private ElectionRound round;

    private Short order;

    private Long candidateFirstId;

    private Short candidateFirstOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ElectionRound getRound() {
        return round;
    }

    public void setRound(ElectionRound round) {
        this.round = round;
    }

    public Short getOrder() {
        return order;
    }

    public void setOrder(Short order) {
        this.order = order;
    }

    public Long getCandidateFirstId() {
        return candidateFirstId;
    }

    public void setCandidateFirstId(Long candidateFirstId) {
        this.candidateFirstId = candidateFirstId;
    }

    public Short getCandidateFirstOrder() {
        return candidateFirstOrder;
    }

    public void setCandidateFirstOrder(Short candidateFirstOrder) {
        this.candidateFirstOrder = candidateFirstOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CandidateDto that = (CandidateDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return round == that.round;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (round != null ? round.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CandidateDto{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", round=" + round +
                ", order=" + order +
                ", candidateFirstId=" + candidateFirstId +
                ", candidateFirstOrder=" + candidateFirstOrder +
                '}';
    }
}
