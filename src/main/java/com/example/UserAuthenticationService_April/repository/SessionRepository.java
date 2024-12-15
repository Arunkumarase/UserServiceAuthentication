package com.example.UserAuthenticationService_April.repository;

import com.example.UserAuthenticationService_April.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
