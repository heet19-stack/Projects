package com.Project5.SpringSecurity.service;

import com.Project5.SpringSecurity.Role;
import com.Project5.SpringSecurity.Status;
import com.Project5.SpringSecurity.entity.SignupRequest;
import com.Project5.SpringSecurity.entity.User;
import com.Project5.SpringSecurity.repository.SignupRequestRepository;
import com.Project5.SpringSecurity.repository.UserRepository;
import com.Project5.SpringSecurity.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SignupService
{
    @Autowired
    private SignupRequestRepository signupRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String signup(SignupRequest request){
        request.setStatus(Status.PENDING);
        request.setCreatedAt(LocalDateTime.now());

        signupRequestRepository.save(request);

        return "Request sent for approval!";
    }
    public String RequestApproval(Long requestId,Role approverRole){
        SignupRequest signupRequest =
                signupRequestRepository
                        .findById(requestId).orElseThrow
                                (() -> new RuntimeException("Request not found"));

        if (approverRole == Role.ROLE_ADMIN){
            //allowed

        } else if (approverRole == Role.ROLE_TEACHER) {
            if (signupRequest.getRole() !=Role.ROLE_STUDENT){
                throw  new RuntimeException("Teacher can only  student approve request");
            }
        }else {
            throw new RuntimeException("Unauthorised!");
        }
        User user = new User();
        user.setUsername(signupRequest.getUsername());
//        user.setPassword(signupRequest.getPassword());
        user.setPassword(
                passwordEncoder.
                        encode(signupRequest.getPassword()));
        user.setRole(signupRequest.getRole());
        user.setEnabled(true);

        userRepository.save(user);
        signupRequestRepository.delete(signupRequest);

        return "Request approve successsfully!";
    }
    public List<SignupRequest> signupRequestList(){

        return signupRequestRepository.findAll();
    }

    public String login(String username,String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }
        if (!user.isEnabled()) {
            throw new RuntimeException("User not approved yet");
        }
//        if (!user.getPassword().equals(password)) {
//            throw new RuntimeException("Invalid password!");
//        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }
        String token = jwtUtil.generateToken(username, user.getRole().name());

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());

            SecurityContextHolder.getContext()
                    .setAuthentication(authToken);
        }
        return token;
    }
}
