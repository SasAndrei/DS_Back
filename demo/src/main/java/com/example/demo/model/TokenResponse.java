package com.example.demo.model;

import java.io.Serializable;

public record TokenResponse(String jwt) implements Serializable {
}
