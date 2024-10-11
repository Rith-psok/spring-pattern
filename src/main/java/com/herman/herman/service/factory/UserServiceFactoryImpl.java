package com.herman.herman.service.factory;

import com.herman.herman.constant.ServiceEnum;
import com.herman.herman.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserServiceFactoryImpl implements UserServiceFactory {
  private final Map<ServiceEnum, IUserService> services;

  @Override
  public IUserService createUserService(ServiceEnum type) {
    final var service = services.get(type);

    if (Objects.isNull(service)) {
      throw new IllegalArgumentException("Unknown user service type: " + type);
    }
    return service;
  }
}
