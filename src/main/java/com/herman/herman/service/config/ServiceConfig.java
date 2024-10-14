package com.herman.herman.service.config;

import com.herman.herman.constant.ServiceEnum;
import com.herman.herman.service.AuthService;
import com.herman.herman.service.SuperAdminService;
import com.herman.herman.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ServiceConfig {

  @Bean
  @SuppressWarnings("unchecked")
  public <T> Map<ServiceEnum, T> userServices(
      UserService userService,
      SuperAdminService superAdminService,
      AuthService authService
  ) {
    return Map.of(
        ServiceEnum.USER_SERVICE, (T) userService,
        ServiceEnum.SUPER_ADMIN_SERVICE, (T) superAdminService,
        ServiceEnum.AUTH_SERVICE, (T) authService
    );
  }
}
