package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.TokenResponse;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtTokenUtil;

//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<UserDto> allUsers() {
        return userRepository.findAll().stream().map((user) -> new UserDto(user.getId(), user.getName(), user.getPassword(), user.getRole())).toList();
    }

    public ResponseEntity<TokenResponse> login(LoginDto loginDto) throws Exception{
        User user = userRepository.findByName(loginDto.name()).orElse(new User());
        if (loginDto.password().equals(user.getPassword()))
            return new ResponseEntity<>(new TokenResponse(jwtTokenUtil.generateToken(user)), HttpStatus.OK);
        else
            return new ResponseEntity<>(new TokenResponse(loginDto.password() + " " + user.getPassword()), HttpStatus.FORBIDDEN);
    }

    public UserDto createUser(UserDto userDto) {
        User newUser = new User(userDto.name(), userDto.password(), userDto.role());
        if (userRepository.findByName(userDto.name()).orElse(null) == null)
            userRepository.save(newUser);
        return userDto;
    }

    public UserDto currentUser(String name) {
        User user =  userRepository.findByName(name).orElse(new User());
        return new UserDto(user.getId(), user.getName(), user.getPassword(), user.getRole());
    }

    public User loadByName(String name) {
        return userRepository.findByName(name).orElse(new User());
    }

    public void deleteUser(Integer uid) {
        User user = userRepository.getReferenceById(uid);
        userRepository.delete(user);
    }
}
