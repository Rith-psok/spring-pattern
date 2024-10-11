package com.herman.herman.config.security;

import com.herman.herman.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ModelMapper modelMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    final var user = this.userRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("User is not found."));
    return this.modelMapper.map(user, UserInfoDetails.class);
  }
}
