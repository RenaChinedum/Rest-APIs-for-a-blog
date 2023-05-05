//package com.task9.task9.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import org.springdoc.core.models.GroupedOpenApi;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//    @Value("${application.version}")
//    private String version;
//    @Bean
//    public OpenAPI api() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Task9")
//                        .description("This Api is to be consumed by front end to implement a social media kind of blog")
//                        .version(version));
//
//    }
//    @Bean
//    public GroupedOpenApi usersEndpoints(){
//        return GroupedOpenApi.builder()
//                .group("users")
//                .pathsToMatch("/users/**")
//                .build();
//    }
//    @Bean
//    public GroupedOpenApi postEndpoints(){
//        return GroupedOpenApi.builder()
//                .group("post")
//                .pathsToMatch("/posts/**")
//                .build();
//    }
//    @Bean
//    public GroupedOpenApi commentEndpoints(){
//        return GroupedOpenApi.builder()
//                .group("comment")
//                .pathsToMatch("/comment/**")
//                .build();
//    }
//    @Bean
//    public GroupedOpenApi likesEndpoints(){
//        return GroupedOpenApi.builder()
//                .group("likes")
//                .pathsToMatch("/likes/**")
//                .build();
//    }
//    @Bean
//    public GroupedOpenApi adminEndpoints(){
//        return GroupedOpenApi.builder()
//                .group("admin")
//                .pathsToMatch("/admin/**")
//                .build();
//    }
//}
