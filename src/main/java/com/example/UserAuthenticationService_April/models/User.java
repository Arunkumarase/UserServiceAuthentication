package com.example.UserAuthenticationService_April.models;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class User extends BaseModel {
    private String email;
    private String password;
    @ManyToMany
    private Set<Role> roles = new HashSet<>();

}
