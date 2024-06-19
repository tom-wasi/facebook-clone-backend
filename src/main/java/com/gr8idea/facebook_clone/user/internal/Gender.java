package com.gr8idea.facebook_clone.user.internal;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("M"), FEMALE("F");

    private final String code;

    private Gender(String code) {
        this.code = code;
    }

}
