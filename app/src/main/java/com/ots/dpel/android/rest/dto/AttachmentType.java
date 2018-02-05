package com.ots.dpel.android.rest.dto;

public enum AttachmentType {
    
    RESULTS(1),
    
    CASHIER(2);

    private final int requestCode;

    AttachmentType(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
