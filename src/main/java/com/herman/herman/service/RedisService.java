package com.herman.herman.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService<T> {
  private final RedisTemplate<String, Object> template;

  public T save(T t, String hashKey, String key) {
    template.opsForHash().put(hashKey, key, t);
    return t;
  }

  public List<Object> findAll(String hashKey) {
    return template.opsForHash().values(hashKey);
  }

  public Object findById(String hashKey, String key) {
    return template.opsForHash().get(hashKey, key);
  }
}
