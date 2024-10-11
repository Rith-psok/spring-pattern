package com.herman.herman.controller;

import com.herman.herman.dto.LoginRequest;
import com.herman.herman.dto.LoginResponse;
import com.herman.herman.dto.UserDto;
import com.herman.herman.dto.UserRequest;
import com.herman.herman.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("/create/user")
  public ResponseEntity<UserDto> save(@RequestBody UserRequest request) {
    return new ResponseEntity<>(this.authService.createUser(request), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    return new ResponseEntity<>(this.authService.login(request), HttpStatus.OK);
  }
}
