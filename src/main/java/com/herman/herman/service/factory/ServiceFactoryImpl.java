package com.herman.herman.service.factory;

import com.herman.herman.constant.ServiceEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ServiceFactoryImpl<T> implements ServiceFactory<T> {
  private final Map<ServiceEnum, T> services;

  @Override
  public T selectService(ServiceEnum type) {
    final var service = services.get(type);

    if (Objects.isNull(service)) {
      throw new IllegalArgumentException("Unknown user service type: " + type);
    }
    return service;
  }
}
