package com.herman.herman.service.config;

import com.herman.herman.constant.ServiceEnum;
import com.herman.herman.service.SuperAdminService;
import com.herman.herman.service.UserService;
import com.herman.herman.service.interfaces.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ServiceConfig {

  @Bean
  public Map<ServiceEnum, IUserService> userServices(
      UserService userService,
      SuperAdminService superAdminService
  ) {
    return Map.of(
        ServiceEnum.USER_SERVICE, userService,
        ServiceEnum.SUPER_ADMIN_SERVICE, superAdminService
    );
  }
}
