package it.epicode.capstone.exceptions;

import lombok.Data;

@Data
public class Error {
    private String message;
    private String details;
    private String status;
}
