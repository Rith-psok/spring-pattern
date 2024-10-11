package com.herman.herman.controller;

import com.herman.herman.constant.ServiceEnum;
import com.herman.herman.dto.UserDto;
import com.herman.herman.dto.UserRequest;
import com.herman.herman.service.factory.UserServiceFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserServiceFactory userServiceFactory;

  @PostMapping
  @Tag(name = "Save user in redis")
  public ResponseEntity<UserDto> save(@RequestBody UserRequest request) {
    log.info("Create a user");
    final var userService = userServiceFactory.createUserService(ServiceEnum.USER_SERVICE);
    return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
  }
}
