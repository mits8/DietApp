package com.example.plan.enums;

public enum Gender {
    Άντρας,
    Γυναίκα,
    Κάτι_Άλλο;

    public boolean isMale() {
        return this == Άντρας;
    }

    public boolean isFemale() {
        return this == Γυναίκα;
    }
}
