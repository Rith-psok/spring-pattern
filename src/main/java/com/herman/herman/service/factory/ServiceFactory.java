package com.herman.herman.service.factory;

import com.herman.herman.constant.ServiceEnum;

public interface ServiceFactory<T> {
  T selectService(ServiceEnum type);
}
