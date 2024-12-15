package com.example.UserAuthenticationService_April.service;

import com.example.UserAuthenticationService_April.models.Session;
import com.example.UserAuthenticationService_April.models.SessionState;
import com.example.UserAuthenticationService_April.models.User;
import com.example.UserAuthenticationService_April.repository.SessionRepository;
import com.example.UserAuthenticationService_April.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.SecretKey;
import javax.swing.text.html.Option;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionRepository sessionRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    public User signup(String email, String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            return null;
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    public Pair<User,MultiValueMap<String,String>> login(String email, String password){

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()){
            return null;
        }

        User user = optionalUser.get();

        if (!bCryptPasswordEncoder.matches(password,user.getPassword())){
            return null;
        }

        /*String message = "{\n" +
                "   \"email\": \"anurag@scaler.com\",\n" +
                "   \"roles\": [\n" +
                "      \"instructor\",\n" +
                "      \"buddy\"\n" +
                "   ],\n" +
                "   \"expirationDate\": \"14thDec2024\"\n" +
                "}";*/
        //byte[] content = message.getBytes(StandardCharsets.UTF_8);
        Map<String, Object> claims = new HashMap<>();
        long timeNowInMilliSeconds = System.currentTimeMillis();
        claims.put("email", user.getEmail());
        claims.put("roles",user.getRoles());
        claims.put("iat", new Date(timeNowInMilliSeconds));
        claims.put("expiring at",new Date(timeNowInMilliSeconds + 10000000));
        MacAlgorithm algorithm = Jwts.SIG.HS256;
        SecretKey secretKey = algorithm.key().build();
        Session session = new Session();
        //String token = Jwts.builder().content(content).signWith(secretKey).compact();
        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        // Storing session for validation
        session.setSessionState(SessionState.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        sessionRepository.save(session);

        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);

        return new Pair<User,MultiValueMap<String,String>>(user, headers);
    }

}
