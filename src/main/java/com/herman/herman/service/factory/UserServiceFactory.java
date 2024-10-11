package com.herman.herman.service.factory;

import com.herman.herman.constant.ServiceEnum;
import com.herman.herman.service.interfaces.IUserService;

public interface UserServiceFactory {
  IUserService createUserService(ServiceEnum type);
}
