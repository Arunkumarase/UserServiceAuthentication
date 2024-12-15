package com.example.UserAuthenticationService_April.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String email;
    private String password;
}
