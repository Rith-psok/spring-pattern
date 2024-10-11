package com.herman.herman.service;

import com.herman.herman.dto.UserDto;
import com.herman.herman.dto.UserRequest;
import com.herman.herman.service.interfaces.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperAdminService implements IUserService {
  @Override
  public UserDto save(UserRequest request) {
    return null;
  }

  @Override
  public UserDto findById(Long id) {
    return null;
  }

  @Override
  public List<UserDto> findAll() {
    return null;
  }

  @Override
  public void delete(Long id) {

  }
}
