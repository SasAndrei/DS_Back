package com.example.demo.dto;

import com.example.demo.model.Role;

public record UserDto(Integer id, String name, String password, Role role) {
}
