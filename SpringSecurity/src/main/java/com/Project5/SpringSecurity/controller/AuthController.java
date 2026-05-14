package com.Project5.SpringSecurity.controller;

import com.Project5.SpringSecurity.Role;
import com.Project5.SpringSecurity.Status;
import com.Project5.SpringSecurity.entity.SignupRequest;
import com.Project5.SpringSecurity.repository.SignupRequestRepository;
import com.Project5.SpringSecurity.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController
{
    @Autowired
    private SignupService signupService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignupRequest request){
        return signupService.signup(request);
    }
    @PutMapping("/admin/{id}")
    public String approveRequestByAdmin(@PathVariable Long id){
        return signupService.RequestApproval(id,Role.ROLE_ADMIN);
    }
    @PutMapping("/teacher/{id}")
    public String approveRequestByTeacher(@PathVariable Long id){
        return signupService.RequestApproval(id,Role.ROLE_TEACHER);
    }
    @GetMapping("/signup/allRequest")
    public List<SignupRequest> signupRequestList(){
        return signupService.signupRequestList();
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,@RequestParam String password){
        return signupService.login(username,password);
    }
    @GetMapping("/hello")
    public String hello(){
        return "Hello!";
    }
}