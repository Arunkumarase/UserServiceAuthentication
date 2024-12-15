package com.example.UserAuthenticationService_April.dto;

import com.example.UserAuthenticationService_April.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class UserDto {
    private String email;
    private Set<Role> roles = new HashSet<>();
}
