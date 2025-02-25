package it.epicode.capstone.authentication;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
