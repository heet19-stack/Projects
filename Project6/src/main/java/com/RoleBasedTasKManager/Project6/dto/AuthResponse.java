package com.RoleBasedTasKManager.Project6.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AuthResponse
{
    private String accessToken;

    private String refreshToken;

    public  AuthResponse(String accessToken,String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
