package com.herman.herman.controller;

import com.herman.herman.constant.ServiceEnum;
import com.herman.herman.dto.UserDto;
import com.herman.herman.dto.UserRequest;
import com.herman.herman.service.factory.ServiceFactory;
import com.herman.herman.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {
  private final ServiceFactory<IUserService> serviceFactory;

  @PostMapping
  @Tag(name = "Save user")
  public ResponseEntity<UserDto> save(@RequestBody UserRequest request) {
    final var superAdminService = serviceFactory.selectService(ServiceEnum.SUPER_ADMIN_SERVICE);
    return new ResponseEntity<>(superAdminService.save(request), HttpStatus.CREATED);
  }
}
