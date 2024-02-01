package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupDTO {
    private String email;
    private String password;
    private String password_confirm;
    private String name;
    private String phone;
    private String birth;
}
