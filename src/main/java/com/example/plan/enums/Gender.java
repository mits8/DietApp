package com.example.plan.enums;

public enum Gender {
    Male,
    Female,
    Something;

    public boolean isMale() {
        return this == Male;
    }

    public boolean isFemale() {
        return this == Female;
    }
}
