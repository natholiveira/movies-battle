package com.letscode.moviesbattle.resources.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@Data
@ConfigurationProperties(prefix = "imdb.api")
public class ImdbProperties {
    public String url;
    public String host;
    public String apiKey;
}
