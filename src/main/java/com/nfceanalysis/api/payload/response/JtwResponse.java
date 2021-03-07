package com.nfceanalysis.api.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JtwResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private String name;
    private List<String> roles;

    public JtwResponse(String accessToken, String id, String username, String email, String name, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.roles = roles;
    }
}
