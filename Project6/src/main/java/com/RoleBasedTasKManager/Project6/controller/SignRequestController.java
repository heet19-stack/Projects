package com.RoleBasedTasKManager.Project6.controller;

import com.RoleBasedTasKManager.Project6.dto.AuthResponse;
import com.RoleBasedTasKManager.Project6.dto.LoginRequestDto;
import com.RoleBasedTasKManager.Project6.dto.RefreshTokenRequest;
import com.RoleBasedTasKManager.Project6.entity.Role;
import com.RoleBasedTasKManager.Project6.entity.SignupRequest;
import com.RoleBasedTasKManager.Project6.security.JwtService;
import com.RoleBasedTasKManager.Project6.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class SignRequestController
{
    @Autowired
    private AuthService authService;
    @Autowired
    JwtService jwtService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest signupRequest,
                         @RequestParam ("role") String role){
        System.out.println("SIGNUP API HIT");
        Role userRole = Role.valueOf(role);

        return authService.signup(signupRequest,userRole);
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequestDto loginRequestDto){
        System.out.println("LOGIN API HIT");

        return authService.login(loginRequestDto);
    }
    @PutMapping("/approveRequest")
    public String approvedRequest(@RequestParam Long id){

        return authService.requestApproval(id);
    }
    @GetMapping("/test")
    public String test(){
        return new BCryptPasswordEncoder().encode("admin123");
    }

    @GetMapping("/signupRequest")
    public List<SignupRequest> getAll(){
        return authService.getSingupRequest();
    }

    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();

        String email = jwtService.extractUsername(refreshToken);

        String newAccessToken =
                jwtService.generateAccessToken(email);

        return new AuthResponse(
          newAccessToken,
          refreshToken
        );
    }
}
