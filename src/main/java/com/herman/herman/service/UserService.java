package com.herman.herman.service;

import com.herman.herman.dto.UserDto;
import com.herman.herman.dto.UserRequest;
import com.herman.herman.model.User;
import com.herman.herman.repository.UserRepository;
import com.herman.herman.service.interfaces.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public UserDto save(UserRequest request) {
    final var user = this.modelMapper.map(request, User.class);
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    final var savedUser = this.userRepository.save(user);
    return this.modelMapper.map(savedUser, UserDto.class);
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

  public UserDto findByEmail(String email) {
    final var user = this.userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User is not found."));
    return this.modelMapper.map(user, UserDto.class);
  }
}
