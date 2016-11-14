package com.boun.swe.semnet.commons.type;

public enum UserStatus {

    SUSPENDED(0),
    ACTIVE(1),
    NEW(2);

    private int value;

    private UserStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
