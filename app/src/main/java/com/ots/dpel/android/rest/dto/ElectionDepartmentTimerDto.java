package com.ots.dpel.android.rest.dto;

/**
 * Created by tasos on 16/11/2017.
 */

public class ElectionDepartmentTimerDto {

    private Long id;
    private Boolean started;
    private Boolean ended;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getStarted() {
        return started;
    }

    public void setStarted(Boolean started) {
        this.started = started;
    }

    public Boolean getEnded() {
        return ended;
    }

    public void setEnded(Boolean ended) {
        this.ended = ended;
    }

    @Override
    public String toString() {
        return "ElectionDepartmentTimerDto{" +
                "id=" + id +
                ", started=" + started +
                ", ended=" + ended +
                '}';
    }
}
