package com.letscode.moviesbattle.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
class SwaggerConfiguration {
    @Bean
    Docket apiDoc() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.letscode.moviesbattle"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Movies Battle")
                .description("Api Movies Battle aims to provide contracts through an API for a quiz where the user will have to hit the best rated movie on IMDb.")
                .version("1.0")
                .license("Apache license version 3.0")
                .licenseUrl("http://www.apache.org/license/license-2-0")
                .contact(new Contact("Nathalia Oliveira", "https://github.com/natholiveira/suricatos", "vo.nathalia@gmail.com"))
                .build();
    }
}
