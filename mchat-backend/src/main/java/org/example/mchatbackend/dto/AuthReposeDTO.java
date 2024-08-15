package org.example.mchatbackend.dto;

import java.util.List;

public class AuthReposeDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private List<String> roles;

    public AuthReposeDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthReposeDTO(String accessToken, String username) {
        this.accessToken = accessToken;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
