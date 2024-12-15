package com.example.UserAuthenticationService_April.controller;

import com.example.UserAuthenticationService_April.dto.LoginRequestDto;
import com.example.UserAuthenticationService_April.dto.LogoutRequestDto;
import com.example.UserAuthenticationService_April.dto.SignupRequestDto;
import com.example.UserAuthenticationService_April.dto.UserDto;
import com.example.UserAuthenticationService_April.models.User;
import com.example.UserAuthenticationService_April.service.AuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupRequestDto signupRequestDto){
        try{
            User user = authService
                    .signup(signupRequestDto.getEmail(), signupRequestDto.getPassword());
            return new ResponseEntity<>(getUserDto(user), HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto){
        try{
            Pair<User, MultiValueMap<String,String>> bodyWithHeaders = authService.
                    login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
            return new ResponseEntity<>(getUserDto(bodyWithHeaders.a),bodyWithHeaders.b,HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public  ResponseEntity<UserDto> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        return null;
    }

    private UserDto getUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
