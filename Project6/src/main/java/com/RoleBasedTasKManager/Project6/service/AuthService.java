package com.RoleBasedTasKManager.Project6.service;

import com.RoleBasedTasKManager.Project6.dto.AuthResponse;
import com.RoleBasedTasKManager.Project6.dto.LoginRequestDto;
import com.RoleBasedTasKManager.Project6.entity.Role;
import com.RoleBasedTasKManager.Project6.entity.SignupRequest;
import com.RoleBasedTasKManager.Project6.entity.Status;
import com.RoleBasedTasKManager.Project6.entity.User;
import com.RoleBasedTasKManager.Project6.repository.SignupRequestRepository;
import com.RoleBasedTasKManager.Project6.repository.UserRepository;
import com.RoleBasedTasKManager.Project6.security.JwtService;
import com.RoleBasedTasKManager.Project6.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthService
{
    @Autowired
    private SignupRequestRepository signupRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public String signup(SignupRequest signupRequest, Role role){
        signupRequest.setStatus(Status.PENDING);
        signupRequest.setCreatedAt(LocalDateTime.now());
        signupRequest.setRole(role);
        System.out.println("Saving request to database!");
        signupRequestRepository.save(signupRequest);
        System.out.println("Request saved!");

        return "Request sent for approval!";
    }

    public AuthResponse login(LoginRequestDto loginRequestDto){
        User user = userRepository.findByEmail(loginRequestDto.getEmail());

        if(user == null){
            throw  new RuntimeException("User not found!");
        }
        if (user.isEnabled() == false){
            throw new RuntimeException("User not approved yet!");
        }
        if(!passwordEncoder.matches(
                loginRequestDto.getPassword(),
                user.getPassword()
        )){
            throw new RuntimeException("Invalid password!");
        }
//        return jwtService.generateToken(user.getEmail());
            String accessToken = jwtService.generateAccessToken(user.getEmail());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail());

            return new AuthResponse(
                    accessToken,refreshToken
            );
        }

         @PreAuthorize("hasRole('ADMIN')")
        public String requestApproval(Long id){

        SignupRequest signupRequest = signupRequestRepository.
                findById(id).orElseThrow(()->new RuntimeException(""));

            User user = new User();

            user.setUsername(signupRequest.getUsername());
            user.setPassword(signupRequest.getPassword());
            user.setEmail(signupRequest.getEmail());
            user.setRole(signupRequest.getRole());
            user.setEnabled(true);

            signupRequestRepository.delete(signupRequest);
            userRepository.save(user);

            return "Request accepted!";
        }
        public List<SignupRequest> getSingupRequest(){
           return signupRequestRepository.findAll();
        }
}
