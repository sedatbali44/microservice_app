package com.sample.orderservice.Rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SpringFoxConfiguration {

    @Bean
    public Docket api() {



        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.sample.orderservice"))
                .paths(PathSelectors.any())
                .build();
    }




    private ApiInfo apiInfo() {
        return new ApiInfo("Order Service API",
                "API that allows to perform CRUD operations for order information stored in a DB.",
                "1.0",
                "",
                new Contact("Pizza", "", ""),
                "Demo",
                "", Collections.emptyList());
    }
}
