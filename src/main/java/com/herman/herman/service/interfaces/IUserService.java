package com.herman.herman.service.interfaces;

import com.herman.herman.dto.UserDto;
import com.herman.herman.dto.UserRequest;

import java.util.List;

public interface IUserService {
  UserDto save(UserRequest request);

  UserDto findById(Long id);

  List<UserDto> findAll();

  void delete(Long id);
}
