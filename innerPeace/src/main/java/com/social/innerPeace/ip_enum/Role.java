package com.social.innerPeace.ip_enum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private String role;

    private String key;

    Role(String role){
        this.role = role;
    }

    public String value(){
        return role;
    }

}
