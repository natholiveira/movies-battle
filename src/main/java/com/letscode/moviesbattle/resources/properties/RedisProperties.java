package com.letscode.moviesbattle.resources.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@Data
@ConfigurationProperties(prefix = "redis.config")
public class RedisProperties {
    String host;
    Integer port;
    Integer ttl;
}
