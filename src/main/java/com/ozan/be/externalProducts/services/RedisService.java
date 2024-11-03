package com.ozan.be.externalProducts.services;

import com.ozan.be.externalProducts.dtos.KasondaPriceResponseDTO;
import com.ozan.be.healthcheck.HealthCheckController;
import com.ozan.be.kasondaApi.dtos.ColorOption;
import com.ozan.be.kasondaApi.dtos.Product;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  public boolean keyExists(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public Object getKeyValue(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void setKeyProductWithTtl(String key, Product product, long ttl) {
    redisTemplate.opsForValue().set(key, product, Duration.ofSeconds(ttl));
  }

  public void setKeyPriceWithTtl(String key, KasondaPriceResponseDTO price, long ttl) {
    redisTemplate.opsForValue().set(key, price, Duration.ofSeconds(ttl));
  }

  public void setKeyColorOptionWithTtl(String key, ColorOption color, long ttl) {
    redisTemplate.opsForValue().set(key, color, Duration.ofSeconds(ttl));
  }

  public void setKeyValueHealthcheck(String key, String value) {
    redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(60));
  }

  public ResponseEntity<HealthCheckController.VersionDTO> healthcheckCache(
      String key, String value) {
    HealthCheckController.VersionDTO versionDTO;
    HealthCheckController.CacheStatus cacheStatus;
    if (keyExists(key)) {
      cacheStatus = HealthCheckController.CacheStatus.HIT;
    } else {
      setKeyValueHealthcheck(key, value);
      cacheStatus = HealthCheckController.CacheStatus.MISS;
    }

    versionDTO =
        HealthCheckController.VersionDTO.builder()
            .cacheStatus(cacheStatus)
            .description(getKeyValue(key).toString())
            .build();
    return ResponseEntity.ok(versionDTO);
  }
}
