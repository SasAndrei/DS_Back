package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.TokenResponse;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.allUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/current")
    public ResponseEntity<?> getCurrentUser(@RequestParam String name) {
        return new ResponseEntity<>(userService.currentUser(name), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        return new ResponseEntity<>(userService.createUser(userDto), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginDto loginDto) throws Exception {
        return userService.login(loginDto);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> delete(@RequestParam Integer user_id) {
        userService.deleteUser(user_id);
        return new ResponseEntity<>("User deleted!", HttpStatus.OK);
    }
}
