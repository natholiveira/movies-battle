package com.letscode.moviesbattle.resources.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@Data
@ConfigurationProperties(prefix = "api.score")
public class ScoreProperties {
    public Long value = 1L;
    public Long maximunAttempts = 3L;
}
