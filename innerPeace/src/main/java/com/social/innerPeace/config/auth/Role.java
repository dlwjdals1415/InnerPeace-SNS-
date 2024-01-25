package com.social.innerPeace.config.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String role;

    private String key;

    Role(String role){
        this.role = role;
    }

    public String value(){
        return role;
    }

}
