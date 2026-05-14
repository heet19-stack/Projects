package com.Project5.SpringSecurity.repository;

import com.Project5.SpringSecurity.entity.SignupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignupRequestRepository extends JpaRepository<SignupRequest,Long> {
}
