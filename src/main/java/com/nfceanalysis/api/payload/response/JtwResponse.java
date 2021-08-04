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
    private String email;
    private String name;
    private List<String> roles;

    public JtwResponse(String accessToken, String email, String id, String name, List<String> roles) {
        this.token = accessToken;
        this.email = email;
        this.id = id;
        this.name = name;
        this.roles = roles;
    }
}
