package com.ozan.be.healthcheck;

import com.ozan.be.common.BaseController;
import com.ozan.be.externalProducts.services.RedisService;
import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/healthcheck")
@AllArgsConstructor
@RestController
public class HealthCheckController extends BaseController {
  private final RedisService redisService;

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class VersionDTO implements Serializable {
    @Builder.Default private Integer VERSION_ID = 5;
    @Builder.Default private Instant now = Instant.now();
    @Builder.Default private CacheStatus cacheStatus = CacheStatus.UNKNOWN;
    @Builder.Default private String description = "-";
  }

  public enum CacheStatus {
    MISS,
    HIT,
    ERROR,
    UNKNOWN
  }

  /*
     healthcheck endpoint that returns version number for AWS services
  */
  @GetMapping
  public ResponseEntity<VersionDTO> healthcheck() {
    return ResponseEntity.ok(new VersionDTO());
  }

  @GetMapping("/cache/{key}/{value}")
  public ResponseEntity<VersionDTO> healthcheckCache(
      @PathVariable("key") String key, @PathVariable("value") String value) {
    try {
      return redisService.healthcheckCache(key, value);
    } catch (Exception e) {
      VersionDTO versionDTO =
          VersionDTO.builder().cacheStatus(CacheStatus.ERROR).description(e.getMessage()).build();
      return ResponseEntity.ok(versionDTO);
    }
  }
}
