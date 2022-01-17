package me.polyfrontier.casparwia2.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.schema.ScalarType;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .globalRequestParameters(List.of(authorizationParameter()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "PolyFrontier API",
                "All routes of the PolyFrontier's API.",
                "1.0",
                null,
                null,
                null, null, Collections.emptyList());
    }

    private RequestParameter authorizationParameter() {
        //Adding Header
        RequestParameterBuilder aParameterBuilder = new RequestParameterBuilder();
        aParameterBuilder.name("Authorization")
                .description("Bearer")
                .required(false)
                .in(ParameterType.HEADER)
                .query(simpleParameterSpecificationBuilder -> simpleParameterSpecificationBuilder
                        .model(modelSpecificationBuilder -> modelSpecificationBuilder.scalarModel(ScalarType.STRING))
                        .defaultValue("Bearer tmp"))
                .build();

        return aParameterBuilder.build();
    }
}