package com.ots.dpel.android.rest.dto;

public enum ElectionRound {
    
    FIRST {
        @Override
        public String toString() {
            return "Α' Γύρος";
        }
    },
    SECOND {
        @Override
        public String toString() {
            return "Β' Γύρος";
        }
    }
    
}
