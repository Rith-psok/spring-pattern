package com.herman.herman.service;

import com.herman.herman.config.security.JwtService;
import com.herman.herman.dto.LoginRequest;
import com.herman.herman.dto.LoginResponse;
import com.herman.herman.dto.UserDto;
import com.herman.herman.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public LoginResponse login(LoginRequest request) {
    final var user = userService.findByEmail(request.getEmail());
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    final var token = jwtService.generateToken(user.getEmail());
    return new LoginResponse(token);
  }

  public UserDto createUser(UserRequest request) {
    return userService.save(request);
  }
}
